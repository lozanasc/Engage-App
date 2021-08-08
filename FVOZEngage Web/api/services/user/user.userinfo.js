const db_conn = require('../../database/db');

module.exports = async(email) => {
    try {
        const result = await db_conn(`SELECT * FROM user WHERE user_email = '${email}'`);
        return result;
    } catch (error) {
        console.trace(error);
    }
}