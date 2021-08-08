// ? Main components
import Login from '../views/Forms/admin.login';
import Registration from '../views/Forms/admin.registration';
import Dashboard from '../views/Dashboard/admin.dashboard';
// ? Dashboard content components
import Users from '../views/Dashboard/Content/dashboard.users';
import Projects from '../views/Dashboard/Content/dashboard.projects';
import Settings from '../views/Dashboard/Content/dashboard.settings';

// ! Don't change anything unless you know what you're doing!
// ? An array of objects that contains route properties for the entire web application
const RouteList = [
    {
        route_type: 'public',
        path: '/',
        redirect_path: '/dashboard',
        exact: true,
        strict: true,
        component: Login
    },
    {
        route_type: 'protected',
        path: '/registration',
        exact: true,
        strict: true,
        component: Registration
    },
    {
        route_type: 'protected',
        path: '/dashboard',
        redirect_path: '/',
        strict: true,
        component: Dashboard,
        routes: [
            {
                route_type: 'protected',
                path: '/dashboard/users',
                redirect_path: '/',
                exact: true,
                strict: true,
                component: Users
            },
            {
                route_type: 'protected',
                path: '/dashboard/projects',
                redirect_path: '/',
                exact: true,
                strict: true,
                component: Projects
            },
            {
                route_type: 'protected',
                path: '/dashboard/settings',
                redirect_path: '/',
                exact: true,
                strict: true,
                component: Settings
            }
        ]
    }
];

export default RouteList;