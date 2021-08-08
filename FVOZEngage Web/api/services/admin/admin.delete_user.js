const db_conn = require('../../database/db');

module.exports = async(user_id) => {
    try {
        await db_conn(`DELETE FROM user WHERE user_id = ${user_id}`);
        return true;
    } catch (error) {
        console.trace(error);
        return false;
    }
}