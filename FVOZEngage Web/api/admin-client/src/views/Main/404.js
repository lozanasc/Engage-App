import React from 'react';
import { Link } from 'react-router-dom';

const NotFound = () => {
    return (
        <div>
            <h1>
                Page not Found!
            </h1>
            <Link
                to = '/'
            >
                Go back to Menu
            </Link>
        </div>
    );
}

export default NotFound;