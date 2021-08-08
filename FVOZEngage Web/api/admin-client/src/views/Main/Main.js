import React from 'react';
import { BrowserRouter as Router, Switch, Redirect } from 'react-router-dom';
import Controller from '../../routes/route.public';
import { AuthProvider } from '../../context/admin.context';
import Routes from '../../routes/routes.list';
import NotFound from './404';

const Main = () => {
    return (
        <AuthProvider>
            <Router>
                <Switch>
                    {
                        Routes.map((routes, key) => 
                            <Controller
                                key = {key}
                                {...routes}
                            />
                        )
                    }
                    <Controller
                        path = '/404'
                        component = {NotFound}
                        route_type = 'public'
                        exact = {true}
                        strict = {true}
                    />
                    <Redirect
                        to = '/404'
                    />
                </Switch>
            </Router>
        </AuthProvider>
    );
}

export default Main;