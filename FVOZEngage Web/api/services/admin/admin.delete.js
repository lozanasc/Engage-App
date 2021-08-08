const db_conn = require('../../database/db');

module.exports = async(admin_email) => {
    try {
        await db_conn(`DELETE FROM admin WHERE email = ${admin_email}`);
        return true;
    } catch (error) {
        console.trace(error);
        return false;
    }
}