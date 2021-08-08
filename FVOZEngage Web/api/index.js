require('dotenv').config();

const express = require('express');
const morgan = require('morgan');
const app = express();
const PORT = process.env.DB_API_SERVER;

// ! Production only
// const cors = require('cors');

const path = require('path');

// ! Production only
// const allowedOrigins = []

app.use(express.json());
app.use(express.urlencoded({extended: true}));
app.use(morgan('dev'));

// ! Production only
// app.use(cors({
//     origin: (origin, callback) => {
// allow requests with no origin 
// (like mobile apps or curl requests)
//         if(!origin) return callback(null, true);
//         if(allowedOrigins.indexOf(origin) === -1){
//             var msg = 'The CORS policy for this site does not ' +
//                       'allow access from the specified Origin.';
//             return callback(new Error(msg), false);
//           }
//           return callback(null, true);
        
//     }
// }));

app.use('/', express.static(path.join(__dirname, 'public')));
app.use('/assets', express.static(path.join(__dirname, 'assets')));
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

const User = require('./routers/routes.user.js');
const Admin = require('./routers/routes.admin.js');
const YT = require('./routers/routes.yt');
const FB = require('./routers/routes.fb');
const Mail = require('./routers/routes.mailer');
const Projects = require('./routers/routes.projects');

app.use('/engage/db_api/v1/user', User);
app.use('/engage/db_api/v1/admin', Admin);
app.use('/engage/yt_api/v1/sermon', YT);
app.use('/engage/fb_api/v1/events', FB);
app.use('/engage/mail_api/v1/request', Mail);
app.use('/engage/project/v1/projects', Projects);

app.listen(PORT, () => {
    console.log(`API Server is listening at PORT = ${PORT}`);
});

