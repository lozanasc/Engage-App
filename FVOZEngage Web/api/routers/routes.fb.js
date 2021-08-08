
const express = require('express');
const router = express.Router();
const req = require('request');

const access_token = process.env.FB_ACCESS_TOKEN;
const page_id = process.env.FB_PAGE_ID;

router.get('/posts', (request, response) => {
    req(`https://graph.facebook.com/v10.0/${page_id}/posts?access_token=${access_token}`,  (error, res, body) => {
        if(error)
            response.status(500).send({'status': false, 'err': error});
        else {
            response.status(200)
            .send({
                'status': res.statusCode,
                'result': JSON.parse(body)
            });
        }
    });
});

router.get('/events', (request, response) => {
    req(`https://graph.facebook.com/v10.0/${page_id}/events?access_token=${access_token}`,  (error, res, body) => {
        if(error)
            response.status(500).send({'status': false, 'err': error});
        else {
            response.status(200)
            .send({
                'status': res.statusCode,
                'result': JSON.parse(body)
            });
        }
    });
});

module.exports = router;