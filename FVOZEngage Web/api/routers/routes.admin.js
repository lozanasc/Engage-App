const express = require('express');
const router = express.Router();
const { compare, hash } = require('bcrypt');
const jwt = require('jsonwebtoken');
const multer = require('multer');
const fs = require('fs');
const upload = multer({dest: 'uploads/'});

const admin_fetch_users = require('../services/admin/admin.users');
const admin_authentication = require('../services/admin/admin.authenticate');
const admin_getinfo = require('../services/admin/admin.userinfo');
const admin_register = require('../services/admin/admin.register');
const admin_del_user = require('../services/admin/admin.delete_user');
const { change_email, change_name, change_password, delete_acc } = require('../services/admin/admin.update');

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
        const { email, password } = request.body;
        const auth = await admin_authentication(email);
        if(auth){
            if(await compare(password, auth[0].admin_password)) {
                const token =jwt.sign({email, password}, process.env.secret_access_token);
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
                    "auth_desc": "Authentication failed, email does not exist in our database!"
                });
        }
    } catch (error) {
        response.status(500)
        .send({
            "thrown_error": error
        });
    }
});

router.post('/agent_info', auth_midware, async(request, response) => {
    try {

	    const {email} = request.body;
        const result = await admin_getinfo(email);

        if(result){
            response.status(200)
            .send({
                "fetch_info": true,
                "fetch_desc": "Success!",
                "fetched_data": result 
            });
        } else {
            response.status(500)
            .send({
                "fetch_info": false,
                "fetch_desc": "Failed!",
                "fetched_data": result
            });
        }
    } catch (error) {
        response.status(500)
        .send({
            "fetch_info": false,
            "thrown_error": error
        });
    }
});

router.post('/delete', auth_midware, async(request, response) => {
    try {
        const {id} = request.body;
        const result = delete_acc(id);

        if(result){
			response.status(200)
			.send({
				"status": true
			});
		} else {
			response.status(500)
                .send({
                    "status": false
                });
		}

    } catch (error) {
        console.trace(error);
        response.status(500)
            .send({
                    "status": false
            });
    }
});

router.get('/users', auth_midware, async(request, response) => {
	try {

		const result = await admin_fetch_users();

		if(result){
			response.status(200)
			.send({
				"fetch_status": true,
				"fetch_data": result
			});
		} else {
			response.status(500)
                .send({
                        "fetch_status": false,
                        "fetch_data": result
                });
		}
        
	} catch (error) {
		response.status(200)
            .send({
                    "fetch_status": false,
                    "fetch_error": error
            });
	}
});

router.post('/register', upload.single('file'), auth_midware, async(request, response) => {
    try {

        const { email, name, password } = request.body;
        const encrypt_pass = await hash(password, 10);

        let file_type = request.file.mimetype.split('/')[1];
        let new_file_name = request.file.filename + '.' + file_type;

        fs.rename(`./uploads/${request.file.filename}`, `./uploads/${new_file_name}`, async() => {
            const registration_response = await admin_register(email, name, encrypt_pass, new_file_name);
            if(registration_response){
                response.status(200)
                .send({
                    "register_status": registration_response,
                    "register_desc": "Success!"
                });
            } else {
                response.status(500)
                .send({
                    "register_status": registration_response,
                    "register_desc": "Failed!"
                });
            }
        });
    } catch (error) {
        response.status(500)
        .send({
            "thrown_error": error
        });
    }
});

router.post('/remove', auth_midware, async(request, response) => {
    try {
        const { user_id } = request.body;
        const delete_result = await admin_del_user(user_id);
        if(delete_result)
            response.status(200).send({"delete_status": true});
        else
            response.status(500).send({"delete_status": false});
    } catch (error) {
        console.trace(error);
        response.status(500).send({"delete_status": false});
    }
});

router.post('/change_password', auth_midware, async(request, response) => {
    
    const {new_password, current_email} = request.body;
    const new_pass_encrypted = await hash(new_password, 10);
    const update_response = await change_password(new_pass_encrypted, current_email);

    if(update_response){
        response.status(200).send({
            "update_status": update_response,
            "update_desc": "Success!",
            "update_type": "password"
        });
    } else {
        response.status(500)
        .send({
            "update_status": update_response,
            "update_desc": "Failed!",
            "update_type": "password"
        });
    }

});

router.post('/change_name', auth_midware, async(request, response) => {
    const { new_name, old_name } = request.body;
    const update_response = await change_name(new_name, old_name);
    if(update_response) {
        response.status(200).send({
            "update_status": update_response,
            "update_desc": "Success!",
            "update_type": "name"
        });
    } else {
        response.status(500).send({
            "update_status": update_response,
            "update_desc": "Failed!",
            "update_type": "name"
        });
    }
});

router.post('/change_email', auth_midware, async(request, response) => {
    const { new_email, old_email } = request.body;
    const update_response = await change_email(new_email, old_email);
    if(update_response) {
        response.status(200).send({
            "update_status": update_response,
            "update_desc": "Success!",
            "update_type": "email"
        });
    } else {
        response.status(500).send({
            "update_status": update_response,
            "update_desc": "Failed!",
            "update_type": "email"
        });
    }
});

module.exports = router;