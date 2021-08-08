import React , { useContext } from 'react';
import toast, { Toaster } from 'react-hot-toast';
import { AuthContext } from '../../../context/admin.context';
import './dashboard.content.css';

const notifyErr = () => toast.error('Account deletion failed, something went wrong!', {
    style: {
        fontFamily: "Montserrat"
    }
});
const notifySucc = (msg) => toast.success(msg, {
    style: {
        fontFamily: "Montserrat, sans-serif"
    }
});

const Settings = () => {

    const {admin_id, admin_name, admin_email, admin_password, admin_profile, Logoff} = useContext(AuthContext);

    const Delete = async(id_arg) => {
        const headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('Authorization', `Bearer ${localStorage.getItem('access_token')}`);
        const delete_req = await fetch('/engage/db_api/v1/admin/delete', {
            method: 'POST',
            headers: headers,
            body: JSON.stringify({
                'id': id_arg
            })
        });

        return delete_req.json();
    }

    return (
        <div className="DashboardContainer SettingBg">
            <Toaster/>
            <div className="LeftPanel SettingSeparate">
                <div className="InfoPanel">
                    <h1>Account Information: </h1>
                    <div className="Profile" style={{'backgroundImage': `url(/uploads/${admin_profile})`}}></div>
                    <p> Id: {admin_id} </p>
                    <p> Name: {admin_name} </p>
                    <p> Email: {admin_email} </p>
                    <p> Password: {admin_password}</p>
                </div>
                <button 
                    className = "Delete"
                    onClick = {() => {
                        Delete(admin_id)
                        .then(result => {
                            notifySucc('Account deleted!');
                            setTimeout(() => {
                                Logoff();
                            }, 1500);
                        })
                        .catch(err => {
                            notifyErr();
                            console.error(err);
                        })
                    }}
                >
                    Delete Account
                </button>
            </div>
            <div className="RightPanel">
                <h3>
                    "Church is not an organization you join; it is a family where you belong, a home where you are loved and a hospital where you find healing." ~ Nicky Gumbel
                </h3>
            </div>
        </div>
    );
}

export default Settings;