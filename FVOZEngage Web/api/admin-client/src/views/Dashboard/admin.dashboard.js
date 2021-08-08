import React, { useContext } from 'react';
import { AuthContext } from '../../context/admin.context';
import { Switch , Link } from 'react-router-dom';
import Controller from '../../routes/route.public';
import Menu from './Content/dashboard.menu';
import Logo from '../../assets/logo/fvoz_nav_logo.png';
import './admin.dashboard.css';
// TODO: Dashboard stuff, please do it.
/*
    ? How we'll do it:
    * This component will house the header or the nav bar
    * Under the the header are the sub dashboard components
*/
const Dashboard = ({routes}) => {

    const Home = window.location.pathname;
    const { Logoff } = useContext(AuthContext);

    return (
        <div className="Container">
            <div className="Header">
                <div className="Logo">
                    <img src={Logo} alt=""/>
                </div>
                <div className="LinkGroup">
                    <Link
                        to = '/dashboard'
                    >
                        Menu
                    </Link>
                    <Link
                        to = '/dashboard/users'
                    >
                        Users
                    </Link>
                    <Link
                        to = '/dashboard/projects'
                    >
                        Projects
                    </Link>
                    <Link
                        to = '/dashboard/settings'
                    >
                        Settings
                    </Link>
                    <Link
                        to = '/'
                        onClick={Logoff}
                    >
                        Logout
                    </Link>
                </div>
            </div>
            <div className="Content">
                {
                    Home === '/dashboard'
                    ?
                    <Menu/>
                    :
                    null
                }
                <Switch>
                    {
                        routes.map((routes, key) => <Controller key = {key} {...routes} />)
                    }
                </Switch>
            </div>
        </div>
    );
}

export default Dashboard;