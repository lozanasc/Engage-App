import React , { useState, useContext, useEffect } from 'react';
import toast, { Toaster } from 'react-hot-toast';
import './dashboard.content.css';
import { AuthContext } from '../../../context/admin.context';

const notifyErr = (msg) => toast.error(msg, {
    style: {
        fontFamily: "Montserrat"
    }
});
const notifySucc = (msg) => toast.success(msg, {
    style: {
        fontFamily: "Montserrat, sans-serif"
    }
});

const Users = () => {

    const {admin_name, get_users, users} = useContext(AuthContext);
    const [ focus_user, set_focus_user ] = useState({});
    const [ focus_is_active, set_focus_is_active ] = useState(false);
    const [ edit_user , set_edit_user ] = useState(false);

    // To edit
    const [ new_email, set_new_email ] = useState('');
    const [ new_name, set_new_name ] = useState('');
    const [ new_password, set_new_password ] = useState('');
    const [ new_contact, set_new_contact ] = useState('');
    const [ new_profile, set_new_profile ] = useState('default.png');
    
    const Delete = async(id_arg) => {
        const headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('Authorization', `Bearer ${localStorage.getItem('access_token')}`);
        const delete_req = await fetch('/engage/db_api/v1/admin/remove', {
            method: 'POST',
            headers: headers,
            body: JSON.stringify({
                'user_id': id_arg
            })
        });

        return delete_req.json();
    }

    const Edit = async(name_arg, email_arg, password_arg, contact_arg, profile_arg) => {

        const headers = new Headers();
        const data = new FormData();

        headers.append('Authorization', `Bearer ${localStorage.getItem('access_token')}`);
        data.append('file', profile_arg);
        data.append('email', email_arg !== '' ? email_arg : focus_user.UserEmail);
        data.append('name', name_arg !== '' ? name_arg : focus_user.UserName);
        data.append('password', password_arg !== '' ? password_arg : focus_user.UserPassword);
        data.append('contact', contact_arg !== '' ? contact_arg : focus_user.UserContact);
        data.append('id', focus_user.UserId);

        const update_req = await fetch('/engage/db_api/v1/user/update', {
            method: 'POST',
            headers: headers,
            body: data
        });

        return update_req.json();
    }

    const HandleChange = async(e) => {
        e.preventDefault();

        Edit(new_name, new_email, new_password, new_contact, new_profile)
        .then(result => {
            notifySucc('Edit successful!');
            setTimeout(() => {
                window.location.reload();
            }, 1500)
        })
        .catch(err => {
            notifyErr('Edit failed, no changes made!');
        })

    }

    useEffect(() => {
        get_users();
    }, []);

    console.log(focus_user.UserProfile);

    return (
        <div className="DashboardContainer">
            <Toaster/>
            <div className="LeftPanel">
                <div className="ListContainer">
                    <h3>Hello, {admin_name}! </h3>
                    <h2> User List: </h2>
                    <div className="List">
                        {
                            users.length <= 0 ?
                            <p>
                                List seems empty 🤔
                            </p>
                            :
                            users.map((items, key) => 
                            <p
                                key = {key}
                                onClick = {() => {
                                
                                    set_focus_user({
                                    'UserId': items.user_id,
                                    'UserName': items.user_name,
                                    'UserEmail': items.user_email,
                                    'UserPassword': items.user_password,
                                    'UserContact': items.user_contact,
                                    'UserProfile': items.user_profile
                                    });

                                    set_focus_is_active(true);

                                }}
                            >
                                <u>
                                    {items.user_name}
                                </u>
                            </p>)
                        }
                    </div>
                </div>
            </div>
            <div className="RightPanel UsersBg">
                    {
                        focus_is_active ?
                            <div className="UserInfoContainer">
                                <h2>
                                    <u>
                                        User Info:
                                    </u>
                                </h2>
                                <div className="Profile" style={{'backgroundImage': `url(/uploads/${focus_user.UserProfile})`}}></div>
                                <span>
                                    Name: {focus_user.UserName}
                                </span>
                                    <br />
                                <span>
                                    Email: {focus_user.UserEmail}
                                </span>
                                    <br />
                                <span>
                                    Password: {focus_user.UserPassword}
                                </span>
                                <div className="ButtonGroup">
                                    <button
                                        className = "Delete"
                                        onClick = {() => {
                                            Delete(focus_user.UserId)
                                            .then(({delete_status}) => {
                                                notifySucc('User deletion is successful!');
                                                setTimeout(() => {window.location.reload()}, 1500);
                                            }
                                            )
                                            .catch(error => {
                                                console.error(error);
                                                notifyErr('Deletion failed, no changes made!');
                                            })
                                        }}
                                    >
                                        Delete Account
                                    </button>
                                    <button
                                        className = "Delete"
                                        onClick = {() => {
                                            set_edit_user(true);
                                            set_focus_is_active(false);
                                        }}
                                        style={{'backgroundColor': 'var(--lightBlue)'}}
                                    >
                                        Edit Account
                                    </button>
                                </div>
                            </div>
                        :
                            edit_user
                            ?
                            <form 
                                    className = "NewProjectForm"
                                    onSubmit = {HandleChange}   
                                    encType = "multipart/form-data"                             
                                >
                                    <h2> Edit User: {focus_user.UserName} </h2>
                                    <div className="InputName">
                                        <label htmlFor="user_image">
                                            User Profile Picture
                                        </label>
                                            <br />
                                        <input 
                                            type="file"
                                            name="user_image" 
                                            id="user_image" 
                                            onChange = { e => set_new_profile(e.target.files[0])}
                                        />
                                    </div>
                                    <div className="InputName">
                                        <label htmlFor="user_email">
                                            New Email from <strong><u>{focus_user.UserEmail}</u></strong>
                                        </label>
                                            <br />
                                        <input 
                                            type="text" 
                                            name="user_email" 
                                            id="user_email" 
                                            onChange = { e => set_new_email(e.target.value)}
                                        />
                                    </div>
                                    <div className="InputName">
                                        <label htmlFor="user_name">
                                            New Name from <strong><u>{focus_user.UserName}</u></strong>
                                        </label>
                                            <br />
                                        <input 
                                            type="text" 
                                            name="user_name" 
                                            id="user_name" 
                                            onChange = { e => set_new_name(e.target.value)}
                                        />
                                    </div>
                                    <div className="InputName">
                                        <label htmlFor="user_password">
                                            New Password from <strong><u>{focus_user.UserPassword}</u></strong>
                                        </label>
                                            <br />
                                        <input 
                                            type="password" 
                                            name="user_password" 
                                            id="user_password" 
                                            onChange = { e => set_new_password(e.target.value)}
                                        />
                                    </div>
                                    <div className="InputName">
                                        <label htmlFor="user_contact">
                                            New Contact from <strong><u>{focus_user.UserContact}</u></strong>
                                        </label>
                                            <br />
                                        <input 
                                            type="number" 
                                            name="user_contact" 
                                            id="user_contact" 
                                            onChange = { e => set_new_contact(e.target.value)}
                                        />
                                    </div>
                                    <button type="submit">
                                        Accept Changes
                                    </button>
                                </form>
                                :
                                null
                    }
            </div>
        </div>
    )
}

export default Users;