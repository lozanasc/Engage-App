const express = require('express');
const router = express.Router();
const jwt = require('jsonwebtoken');
const multer = require('multer');
const fs = require('fs');
const upload = multer({dest: 'uploads/'});

const get_list = require('../services/projects/projects.list');
const new_proj = require('../services/projects/projects.new');
const edit_proj = require('../services/projects/projects.update');
const del_project = require('../services/projects/projects.delete');


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

router.get('/list', auth_midware, async(request, response) => {
    try {
        const list = await get_list();
        if(list){
            response.status(200).send({
                'status': 'Success!',
                'list': list
            });
        } else {
            response.status(500).send({
                'status': 'Failed!',
                'list': []
            });
        }
    } catch (error) {
        console.trace(error);
    }
});

router.post('/new', upload.single('file'), auth_midware, async(request, response) => {
    try {
        const { name, description } = request.body;
        
        let file_type = request.file.mimetype.split('/')[1];
        let new_file_name = request.file.filename + '.' + file_type;

        fs.rename(`./uploads/${request.file.filename}`, `./uploads/${new_file_name}`, async() => {
            const new_project = await new_proj( name, description, new_file_name);
            if(new_project){
                response.status(200).send({
                    'status': 'Success!'
                });
            } else {
                response.status(500).send({
                    'status': 'Failed!'
                });
            }
        });

    } catch (error) {
        console.trace(error);
    }
});

router.post('/edit', upload.single('file'), auth_midware, async(request, response) => {
    try {
        const { id, name, description } = request.body;
        
        let file_type = request.file.mimetype.split('/')[1];
        let new_file_name = request.file.filename + '.' + file_type;
        
        fs.rename(`./uploads/${request.file.filename}`, `./uploads/${new_file_name}`, async() => {
            const edit = await edit_proj(id, name, description, new_file_name);
            if(edit){
                response.status(200).send({
                    'status': 'Success!'
                });
            } else {
                response.status(500).send({
                    'status': 'Failed!'
                });
            }
        });

    } catch (error) {
        console.trace(error);
    }
});

router.post('/delete', auth_midware, async(request, response) => {
    try {
        const { id , file } = request.body;

        fs.unlink(`./uploads/${file}`, async() => {
            const del = await del_project(id);
            if(del){
                response.status(200).send({
                    'status': 'Success!'
                });
            } else {
                response.status(500).send({
                    'status': 'Failed!'
                });
            }
        })
        
    } catch (error) {
        console.trace(error);
    }
});

module.exports = router;