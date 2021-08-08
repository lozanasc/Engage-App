import React, {useContext} from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../../context/admin.context';
import './dashboard.content.css';
const Menu = () => {

    const {admin_name} = useContext(AuthContext);

    return (
        <div className="DashboardContainer Menu">
            <div className="LeftPanel">
                <div className="Welcome">
                    <h3>Welcome, {admin_name ? admin_name + '!' : 'loading...'}</h3>
                    <h1> FVOZ Engage Admin Dashboard </h1>
                    <h2><u>You can start with:</u></h2>
                    <Link
                        to = "/dashboard/users"
                    >
                        Users
                    </Link>
                    <br />
                    <Link
                        to = "/dashboard/projects"
                    >
                        Projects
                    </Link>
                    <br />
                    <Link
                        to = "/dashboard/settings"
                    >
                        Settings
                    </Link>
                </div>
            </div>
            <div className="RightPanel MenuBg">    
                
            </div>
        </div>
    );
}

export default Menu;