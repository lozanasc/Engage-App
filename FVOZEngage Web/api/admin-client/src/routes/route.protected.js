import React , { useContext } from 'react';
import { Route , Redirect } from 'react-router-dom';
import { AuthContext } from '../context/admin.context';

const Protected = (route) => {

    const {auth_status} = useContext(AuthContext);

    return (
        <Route
            path = {route.path}
            render = {
                renderProps => 
                    auth_status ? 
                        <route.component
                            {...renderProps}
                            routes = {route.routes}
                        />
                    :
                        <Redirect
                            to = {{pathname: route.redirect_path}}
                        />

            }
        />
    );
}

export default Protected;