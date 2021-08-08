import React, { useState } from 'react';
import { Redirect } from 'react-router';
import toast, { Toaster } from 'react-hot-toast';
import Logo from '../../assets/logo/fvoz_logo_light.png';

const notifyErr = () => toast.error('Something went wrong, please recheck fields!', {
    style: {
        fontFamily: "Montserrat"
    }
});

const notifySucc = (msg) => toast.success(msg, {
    style: {
        fontFamily: "Montserrat"
    }
});

const Register = () => {

    const [profile, set_profile] = useState({});
    const [email, set_email] = useState('');
    const [name, set_name] = useState('');
    const [password, set_password] = useState('');
    const [success, set_success] = useState(false);

    const Register = async(email_arg, name_arg, password_arg, profile_arg) => {
        
        const headers = new Headers();
        const data = new FormData();

        headers.append('Authorization', `Bearer ${localStorage.getItem('access_token')}`);
        
        data.append('file', profile_arg);
        data.append('email', email_arg);
        data.append('name', name_arg);
        data.append('password', password_arg);

        const Registration = await fetch('/engage/db_api/v1/admin/register', {
            method: 'POST',
            headers: headers,
            body: data
        });

        return Registration.json();
    }

    const HandleChange = async(e) => {
        e.preventDefault();

        const { register_status, register_desc } = await Register(email, name, password, profile);

        if(register_status) {
            notifySucc("Registration "+register_desc);
            setTimeout(() => {
                set_success(register_status);
            }, 1500)
        } else 
            notifyErr();
    }

    return (
        success ?
        <Redirect
            to = '/'
        />
            :
        <form 
            className = "FormContainer Bg"
            onSubmit = {HandleChange}
            encType = "multipart/form-data"
        >
            <Toaster/>
            <div className="Inner RegistrationSize">
                <div className="Form">
                    <img src={Logo} alt="" className = "Logo"/>
                    <div className="InputProfile">
                        <label htmlFor="profile">
                            Profile Picture
                        </label>
                            <br />
                        <input 
                            type="file" 
                            name="profile" 
                            id="profile" 
                            onChange = { e => set_profile(e.target.files[0])}    
                        />
                    </div>
                    <div className="InputEmail">
                        <label htmlFor="email">
                            Email
                        </label>
                            <br />
                        <input 
                            type="email" 
                            name="email" 
                            id="email" 
                            onChange = { e => set_email(e.target.value)}    
                        />
                    </div>
                    <div className="InputName">
                        <label htmlFor="name">
                            Fullname
                        </label>
                            <br />
                        <input 
                            type="name" 
                            name="name" 
                            id="name" 
                            onChange = { e => set_name(e.target.value)}    
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
                            onChange = { e => set_password(e.target.value)}    
                        />
                    </div>
                    <button type="submit"> REGISTER </button>
                </div>
            </div>
        </form>
    );
}

export default Register;