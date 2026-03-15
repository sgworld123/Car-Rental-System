package com.CarRentalSystem.PaymentService.Service;

import com.CarRentalSystem.PaymentService.Dto.*;
import com.CarRentalSystem.PaymentService.Errors.Exceptions;
import com.CarRentalSystem.PaymentService.Models.Payment;
import com.CarRentalSystem.PaymentService.Models.PaymentStatus;
import com.CarRentalSystem.PaymentService.Repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private PaymentEventPublisher paymentEventPublisher;
    @Mock private RefundEventPublisher refundEventPublisher;

    @InjectMocks private PaymentService paymentService;

    private PaymentMessageDto paymentMsg;

    @BeforeEach
    void setUp() {
        paymentMsg = PaymentMessageDto.builder()
                .bookingId("booking-001")
                .userId("user-001")
                .amount(1500.0)
                .build();
    }

    // ─────────────────────────────────────────────
    // processPayment – SUCCESS
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("processPayment – SUCCESS result publishes success event and saves SUCCESS payment")
    void processPayment_success_publishesSuccessEventAndSaves() {
        PaymentResult successResult = PaymentResult.builder()
                .status(PaymentStatus.SUCCESS)
                .transactionId("txn-123")
                .timestamp(LocalDateTime.now())
                .build();

        try (MockedStatic<MockPaymentProvider> mock = mockStatic(MockPaymentProvider.class)) {
            mock.when(() -> MockPaymentProvider.processPayment(1500.0)).thenReturn(successResult);

            paymentService.processPayment(paymentMsg);

            // Verify success event published with correct bookingId
            ArgumentCaptor<PaymentSuccessEvent> captor = ArgumentCaptor.forClass(PaymentSuccessEvent.class);
            verify(paymentEventPublisher).publishSuccess(captor.capture());
            assertThat(captor.getValue().getBookingId()).isEqualTo("booking-001");
            assertThat(captor.getValue().getAmount()).isEqualTo(1500.0);

            // Verify failure NOT published
            verify(paymentEventPublisher, never()).publishFailure(any());

            // Verify payment saved with SUCCESS status
            ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
            verify(paymentRepository).save(paymentCaptor.capture());
            assertThat(paymentCaptor.getValue().getStatus()).isEqualTo(PaymentStatus.SUCCESS);
        }
    }

    // ─────────────────────────────────────────────
    // processPayment – FAILURE
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("processPayment – FAILURE result publishes failure event and saves FAILURE payment")
    void processPayment_failure_publishesFailureEventAndSaves() {
        PaymentResult failResult = PaymentResult.builder()
                .status(PaymentStatus.FAILURE)
                .failureReason("Insufficient funds")
                .timestamp(LocalDateTime.now())
                .build();

        try (MockedStatic<MockPaymentProvider> mock = mockStatic(MockPaymentProvider.class)) {
            mock.when(() -> MockPaymentProvider.processPayment(1500.0)).thenReturn(failResult);

            paymentService.processPayment(paymentMsg);

            verify(paymentEventPublisher).publishFailure(any());
            verify(paymentEventPublisher, never()).publishSuccess(any());

            ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
            verify(paymentRepository).save(captor.capture());
            assertThat(captor.getValue().getStatus()).isEqualTo(PaymentStatus.FAILURE);
        }
    }

    // ─────────────────────────────────────────────
    // processPayment – UNKNOWN_ERROR (gateway exception)
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("processPayment – PaymentProcessingException sets UNKNOWN_ERROR and still saves payment")
    void processPayment_gatewayException_savesUnknownError() {
        try (MockedStatic<MockPaymentProvider> mock = mockStatic(MockPaymentProvider.class)) {
            mock.when(() -> MockPaymentProvider.processPayment(anyDouble()))
                    .thenThrow(new Exceptions.PaymentProcessingException("Gateway timeout"));

            paymentService.processPayment(paymentMsg);

            // No events should be published
            verify(paymentEventPublisher, never()).publishSuccess(any());
            verify(paymentEventPublisher, never()).publishFailure(any());

            // Payment still saved with UNKNOWN_ERROR
            ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
            verify(paymentRepository).save(captor.capture());
            assertThat(captor.getValue().getStatus()).isEqualTo(PaymentStatus.UNKNOWN_ERROR);
        }
    }

    @Test
    @DisplayName("processPayment – payment record has correct userId and amount")
    void processPayment_paymentRecord_hasCorrectFields() {
        PaymentResult successResult = PaymentResult.builder()
                .status(PaymentStatus.SUCCESS)
                .transactionId("txn-xyz")
                .timestamp(LocalDateTime.now())
                .build();

        try (MockedStatic<MockPaymentProvider> mock = mockStatic(MockPaymentProvider.class)) {
            mock.when(() -> MockPaymentProvider.processPayment(anyDouble())).thenReturn(successResult);

            paymentService.processPayment(paymentMsg);

            ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
            verify(paymentRepository).save(captor.capture());
            Payment saved = captor.getValue();

            assertThat(saved.getUserId()).isEqualTo("user-001");
            assertThat(saved.getAmount()).isEqualTo(1500.0);
            assertThat(saved.getPaymentId()).isNotNull();
            assertThat(saved.getUpdatedAt()).isNotNull();
        }
    }

    // ─────────────────────────────────────────────
    // initiateRefund – SUCCESS
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("initiateRefund – SUCCESS result publishes REFUND_SUCCESS event and saves")
    void initiateRefund_success_publishesRefundSuccessEvent() {
        RefundResult successRefund = RefundResult.builder()
                .refundId("ref-001")
                .status(PaymentStatus.SUCCESS)
                .amount(1500.0)
                .build();

        try (MockedStatic<MockRefundProvider> mock = mockStatic(MockRefundProvider.class)) {
            mock.when(() -> MockRefundProvider.processRefund(1500.0, "booking-001"))
                    .thenReturn(successRefund);

            paymentService.initiateRefund(paymentMsg);

            ArgumentCaptor<RefundStatus> captor = ArgumentCaptor.forClass(RefundStatus.class);
            verify(refundEventPublisher).publishRefundResult(captor.capture());
            assertThat(captor.getValue().getStatus()).isEqualTo(PaymentStatus.REFUND_SUCCESS);
            assertThat(captor.getValue().getBookingId()).isEqualTo("booking-001");

            ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
            verify(paymentRepository).save(paymentCaptor.capture());
            assertThat(paymentCaptor.getValue().getStatus()).isEqualTo(PaymentStatus.REFUND_SUCCESS);
        }
    }

    // ─────────────────────────────────────────────
    // initiateRefund – FAILURE
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("initiateRefund – FAILURE result publishes REFUND_FAILURE event and saves")
    void initiateRefund_failure_publishesRefundFailureEvent() {
        RefundResult failRefund = RefundResult.builder()
                .refundId("ref-002")
                .status(PaymentStatus.FAILURE)
                .amount(1500.0)
                .build();

        try (MockedStatic<MockRefundProvider> mock = mockStatic(MockRefundProvider.class)) {
            mock.when(() -> MockRefundProvider.processRefund(anyDouble(), anyString()))
                    .thenReturn(failRefund);

            paymentService.initiateRefund(paymentMsg);

            ArgumentCaptor<RefundStatus> captor = ArgumentCaptor.forClass(RefundStatus.class);
            verify(refundEventPublisher).publishRefundResult(captor.capture());
            assertThat(captor.getValue().getStatus()).isEqualTo(PaymentStatus.REFUND_FAILURE);

            ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
            verify(paymentRepository).save(paymentCaptor.capture());
            assertThat(paymentCaptor.getValue().getStatus()).isEqualTo(PaymentStatus.REFUND_FAILURE);
        }
    }

    // ─────────────────────────────────────────────
    // initiateRefund – UNKNOWN_ERROR
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("initiateRefund – unknown status publishes UNKNOWN_ERROR event")
    void initiateRefund_unknownStatus_publishesUnknownError() {
        RefundResult unknownRefund = RefundResult.builder()
                .refundId("ref-003")
                .status(PaymentStatus.UNKNOWN_ERROR)
                .amount(1500.0)
                .build();

        try (MockedStatic<MockRefundProvider> mock = mockStatic(MockRefundProvider.class)) {
            mock.when(() -> MockRefundProvider.processRefund(anyDouble(), anyString()))
                    .thenReturn(unknownRefund);

            paymentService.initiateRefund(paymentMsg);

            ArgumentCaptor<RefundStatus> captor = ArgumentCaptor.forClass(RefundStatus.class);
            verify(refundEventPublisher).publishRefundResult(captor.capture());
            assertThat(captor.getValue().getStatus()).isEqualTo(PaymentStatus.UNKNOWN_ERROR);
        }
    }

    @Test
    @DisplayName("initiateRefund – PaymentProcessingException logs error and does NOT save or publish")
    void initiateRefund_exception_doesNotSaveOrPublish() {
        try (MockedStatic<MockRefundProvider> mock = mockStatic(MockRefundProvider.class)) {
            mock.when(() -> MockRefundProvider.processRefund(anyDouble(), anyString()))
                    .thenThrow(new Exceptions.PaymentProcessingException("Refund gateway down"));

            paymentService.initiateRefund(paymentMsg);

            verify(refundEventPublisher, never()).publishRefundResult(any());
            verify(paymentRepository, never()).save(any());
        }
    }
}