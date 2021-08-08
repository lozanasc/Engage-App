import React , { useContext } from 'react';
import ProtectedRoute from './route.protected';
import { AuthContext } from '../context/admin.context';
import { Route, Redirect } from 'react-router-dom';

const Controller = (route) => {
    
    const {auth_status} = useContext(AuthContext);

    return (
        <>
            {
                route.route_type === 'public' &&
                <Route
                    path = {route.path}
                    render = {routeProps => (
                        !auth_status ?
                        <route.component {...routeProps} routes = {route.routes} />
                            :
                        <Redirect
                            to = {route.redirect_path}
                        />
                    )}
                />
            }
            {(route.route_type === 'protected') && <ProtectedRoute {...route} />}
        </>
    );
}

export default Controller;