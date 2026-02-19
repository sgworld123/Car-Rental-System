import { useState } from "react";
import { register as registerFun } from '../Services/authService';

export function useRegister()
{
    const[loading,setLoading] = useState(false);
    const[error,setError] = useState(null);
    const register = async(formData) => {
        setLoading(true);
        setError(null)
        try{
            console.log("calling backend to register user with data:", formData);
            const response = await registerFun({username: formData.username, password: formData.password,
                email:formData.email,phone:formData.phone
            });
            return true;
        }
        catch(err){
            setError(err.response?.data?.message || 'Registration failed');
            return false;
        }
        finally{
            setLoading(false);
        }
    }
    return {register,loading,error};
}