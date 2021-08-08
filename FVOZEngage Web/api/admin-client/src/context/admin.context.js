import { createContext , useState } from "react";

export const AuthContext = createContext();

export const AuthProvider = (props) => {

    const [ auth_status, set_auth_status ] = useState(localStorage.getItem('auth_status'));
    const [ admin_id, set_admin_id ] = useState(localStorage.getItem('id'));
    const [ admin_name, set_admin_name ] = useState(localStorage.getItem('name'));
    const [ admin_email, set_admin_email ] = useState(localStorage.getItem('email'));
    const [ admin_password, set_admin_password ] = useState(localStorage.getItem('password'));
    const [ admin_profile, set_admin_profile ] = useState(localStorage.getItem('profile'));
    const [ users , set_users ] = useState([]);
    const [ projects, set_projects ] = useState([]);
    
    const async_get_projects = async() => {
        const headers = new Headers();
        headers.append('Authorization', `Bearer ${localStorage.getItem('access_token')}`);
        const projects = await fetch('/engage/project/v1/projects/list', {
            method: 'GET',
            headers: headers
        });

        return projects.json();
    }

    const async_get_users = async() => {
        const headers = new Headers();
        headers.append('Authorization', `Bearer ${localStorage.getItem('access_token')}`);

        const users = await fetch('/engage/db_api/v1/admin/users', {
            method: 'GET',
            headers: headers
        });

        return users.json();
    }

    const get_users = () => {

        async_get_users()
        .then(({fetch_data}) => set_users(fetch_data))
        .catch(err => console.error(err));

    }

    const get_projects = () => {

        async_get_projects()
        .then(({list}) => set_projects(list))
        .catch(err => console.error(err));

    }

    const get_info = async(email_arg) => {
        const headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('Authorization', `Bearer ${localStorage.getItem('access_token')}`);
        const info = await fetch('/engage/db_api/v1/admin/agent_info', {
            method: 'POST',
            headers: headers,
            body: JSON.stringify({
                'email': email_arg
            })
        });

        return info.json();
    }

    const Logon = (status, email) => {
        set_auth_status(status);
        localStorage.setItem('auth_status', status);
        setTimeout(() => {
            get_info(email).then(({fetched_data}) => {
                set_admin_id(fetched_data[0].admin_id);
                localStorage.setItem('id', fetched_data[0].admin_id);
                set_admin_name(fetched_data[0].admin_name);
                localStorage.setItem('name', fetched_data[0].admin_name);
                set_admin_email(fetched_data[0].admin_email);
                localStorage.setItem('email', fetched_data[0].admin_email);
                set_admin_password(fetched_data[0].admin_password);
                localStorage.setItem('password', fetched_data[0].admin_password);
                set_admin_profile(fetched_data[0].admin_profile);
                localStorage.setItem('profile', fetched_data[0].admin_profile);
            })
            .catch(err => console.error(err));
        }, 1500);
    }

    const Logoff = () => {
        set_auth_status(false);
        set_admin_name('');
        localStorage.removeItem('auth_status');
        localStorage.removeItem('access_token');
        localStorage.removeItem('id');
        localStorage.removeItem('name');
        localStorage.removeItem('email');
        localStorage.removeItem('password');
    }

    return (
        <AuthContext.Provider
            value = {{
                Logon,
                Logoff,
                auth_status,
                get_users,
                get_projects,
                users,
                projects,
                admin_id,
                admin_name,
                admin_email,
                admin_profile,
                admin_password
            }}
        >
            {props.children}
        </AuthContext.Provider>
    );



}