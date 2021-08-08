const db_conn = require('../../database/db');

module.exports = async(email) => {
    try {
        const query = `SELECT admin_password FROM admin WHERE admin_email = '${email}' `;
        const auth_result = await db_conn(query);
        return auth_result;
    } catch (error) {
        console.trace('Something went wrong, ',error);
        return false;
    }
}