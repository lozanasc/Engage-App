const db_conn = require('../../database/db');

module.exports = async(email) => {
    try {
        const Query = `SELECT user_password FROM user WHERE user_email = '${email}'`;
        const Result = await db_conn(Query);
        return Result;
    } catch (error) {
        console.trace(error);
        return false;
    }
}