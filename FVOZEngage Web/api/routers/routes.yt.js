const express = require('express');
const router = express.Router();
const ytdl = require('ytdl-core');
const ytpl = require('ytpl');
const fs = require('fs');

// ? Testing...
router.post('/download', async(request, response) => {
    try {
        const { url } = request.body;
        if(url){
            ytdl(url)
            .pipe(fs.createWriteStream('video.mp4'));
                response.status(200)
                .send({"status": "Success!"});
        } else {
            response.status(500)
            .send({"status": "Failed!"});
        }
    } catch (error) {
        console.trace(error);
    }
});

router.get('/sermons', async(request, response) => {
    try {
        
        const sermon_list = await ytpl(process.env.PLAYLIST_ID);

        if(sermon_list){
            response.status(200)
            .send(sermon_list.items);
        }
        else {
            response.status(500)
            .send({
                "fetch_sermon_desc": "Failed!"
            })
        }
    } catch (error) {
        console.trace(error);
    }
});

module.exports = router;