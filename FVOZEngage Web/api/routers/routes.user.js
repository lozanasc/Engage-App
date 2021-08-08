const express = require('express');
const router = express.Router();
const { compare, hash } = require('bcrypt');
const jwt = require('jsonwebtoken');
const multer = require('multer');
const fs = require('fs');
const upload = multer({dest: 'uploads/'});

// services
const user_authentication = require('../services/user/user.authenticate');
const user_info = require('../services/user/user.userinfo');
const user_registration = require('../services/user/user.register');
const user_update = require('../services/user/user.update');

const auth_midware = (request, response, next) => {
    const header = request.headers['authorization'];
    const token = header && header.split(' ')[1];
    token == null ?
    response.sendStatus(401) 
    :
    jwt.verify(token, process.env.secret_access_token, (err , {email, password}) => {
        err ?
        response.sendStatus(401) :
        request.email = email;
        request.password = password;
        next();
    });
}

router.post('/auth', async(request, response) => {
    try {
        const { email , password } = request.body;
        const auth = await user_authentication(email);
        if(auth){
            if (await compare(password, auth[0].user_password)){
                const token = jwt.sign({email, password}, process.env.secret_access_token);
                response.status(200)
                .send({
                    "auth_status": true,
                    "auth_desc": "Authentication success!",
                    "email": email,
                    "token": token
                });
            } else {
                response.status(403)
                .send({
                    "auth_status": false,
                    "auth_desc": "Authentication failed!"
                });
            }
        } else {
            response.status(500)
            .send({
                "auth_status": false,
                "auth_desc": "Authentication failed, email not found!"
            })
        }
    } catch (error) {
        console.trace(error);
    }
});

router.post('/info', auth_midware, async(request, response) => {
    try {
        const { user_email } = request.body;
        const info = await user_info(user_email);
        if(info){
            response.status(200)
                .send({
                    "status": true,
                    "data": info,
                });
        } else {
            response.status(403)
                .send({
                    "status": false,
                    "data": [],
                });
        }
    } catch (error) {
        console.trace(error);
    }
});

// ? Profile for User is doable but unlikely to be implemented for now
// upload.single('file'),
router.post('/register',  async(request, response) => {
    const {email, name, contact, password} = request.body;
    const encrypt_pass = await hash(password, 10);
    
    // TODO: Figure out how to send data as multipart/form-data in Android
    // let file_type = request.file.mimetype.split('/')[1];
    // let new_file_name = request.file.filename + '.' + file_type;
    // fs.rename(`./uploads/${request.file.filename}`, `./uploads/${new_file_name}`, async() => {
    // });

    const registration_response = await user_registration(email, name, contact, encrypt_pass, null);
    if(registration_response){
        response.status(200)
        .send({
            "register_status": registration_response,
            "register_desc": "Success!"
        });
    }
    else {
        response.status(500)
        .send({
            "register_status": registration_response,
            "register_desc": "Failed!"
        });
    }
});

// ! PC only for now 😥
router.post('/update', upload.single('file'), auth_midware, async(request, response) => {
    try {
        const { id, email, name, contact, password } = request.body;
        const new_pass_encrypted = await hash(password, 10);
        
        let file_type = request.file.mimetype.split('/')[1];
        let new_file_name = request.file.filename + '.' + file_type;
        
        fs.rename(`./uploads/${request.file.filename}`, `./uploads/${new_file_name}`, async() => {
            const update_response = await user_update(id, email, name, new_pass_encrypted, contact, new_file_name);
            if(update_response){
                response.status(200).send({
                    "update_status": update_response,
                    "update_desc": "Success!",
                });
            } else {
                response.status(500)
                .send({
                    "update_status": update_response,
                    "update_desc": "Failed!",
                });
            }
        });

    } catch (error) {
        response.status(500)
        .send({
            "error": error
        });
    }
});

router.post('/update_mobile', auth_midware, async(request, response) => {
    try {
        const { id, email, name, contact, password } = request.body;
        const new_pass_encrypted = await hash(password, 10);
        const update_response = await user_update(id, email, name, new_pass_encrypted, contact, null);
        if(update_response){
            response.status(200).send({
                "update_status": update_response,
                "update_desc": "Success!",
            });
        } else {
            response.status(500)
            .send({
                "update_status": update_response,
                "update_desc": "Failed!",
            });
        }
    } catch (error) {
        response.status(500)
        .send({
            "error": error
        });
    }
});

module.exports = router;