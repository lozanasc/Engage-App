import React, { useState, useContext } from 'react';
import toast, { Toaster } from 'react-hot-toast';
import './admin.forms.css'
import Logo from '../../assets/logo/fvoz_logo_light.png';
import { AuthContext } from '../../context/admin.context';

const notifyErr = () => toast.error('Login failed, check email and password!', {
    style: {
        fontFamily: "Montserrat"
    }
});
const notifySucc = (msg) => toast.success(msg, {
    style: {
        fontFamily: "Montserrat, sans-serif"
    }
});

const Login = () => {

    const { Logon } = useContext(AuthContext);

    const [ Email, SetEmail ] = useState('');
    const [ Password, SetPassword ] = useState('');

    /**
     * @param {String} email_arg User email as arg
     * @param {String} password_arg User password as arg
     * @returns {Promise}
     */
    const Auth = async(email_arg, password_arg) => {
        const Authenticate = await fetch('/engage/db_api/v1/admin/auth', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                'email': email_arg,
                'password': password_arg
            })
        });
        return Authenticate.json();
    }

    const HandleChange = async(e) => {
        e.preventDefault();
        const { auth_status, auth_desc, email, token } = await Auth(Email, Password);
        if(auth_status){
            notifySucc(auth_desc);
            setTimeout(() => {
                Logon(auth_status, email);
                localStorage.setItem('email', email);
                localStorage.setItem('access_token', token);
            }, 2500)
        } else {
            notifyErr();
        }
    }

    return (
        <form
            onSubmit = {HandleChange}
            className="FormContainer Bg"
        >
            <Toaster/>
            <div className="Inner LoginSize">
                <div className="Form">
                    <img src={Logo} alt="" className = "Logo"/>
                    <div className="InputEmail">
                        <label htmlFor="email">
                            Email
                        </label>
                            <br />
                        <input 
                            type="email" 
                            name="email" 
                            id="email" 
                            onChange = { e => SetEmail(e.target.value)}    
                        />
                    </div>
                    <div className="InputPassword">
                        <label htmlFor="password">
                            Password
                        </label>
                            <br />
                        <input 
                            type="password" 
                            name="password" 
                            id="password" 
                            onChange = { e => SetPassword(e.target.value)}    
                        />
                    </div>
                    <button type="submit"> LOGIN </button>
                </div>
            </div>
        </form>
    );
}

export default Login;