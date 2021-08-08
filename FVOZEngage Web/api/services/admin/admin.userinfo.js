const db_conn = require('../../database/db');

module.exports = async(email) => {
    try {
        const query = `SELECT * FROM admin WHERE admin_email = '${email}'`;
        const result_info = await db_conn(query);
        return result_info;
    } catch (error) {
        console.trace(error);
        return [];
    }
}
