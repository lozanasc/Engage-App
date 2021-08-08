const express = require('express');
const router = express.Router();
const mailer = require('nodemailer');

router.post('/mail', async(request, response) => {
    try {
        const { subject, message } = request.body;
        const transporter = mailer.createTransport({
            host: process.env.SMTP_HOST,
            port: process.env.SMTP_PORT,
            auth: {
                user: process.env.SMTP_USER,
                pass: process.env.SMTP_PASSWORD
            }});
        const sendmail = await transporter.sendMail({
            from: process.env.SMTP_USER, 
            to: "fvoz.engage2021@gmail.com", 
            subject: subject, 
            text: message
        });

        if(sendmail.accepted){
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
    }
});

module.exports = router;
