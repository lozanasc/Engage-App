const db_conn = require('../../database/db');
module.exports = async(id, email, name, password, contact, profile) => {
    try {
        const query = `UPDATE user SET 
                        user_email = '${email}', 
                        user_name = '${name}',
                        user_password = '${password}',
                        user_contact = '${contact}',
                        user_profile = '${profile}' 
                        WHERE
                        user_id = ${id}
                        `;
        await db_conn(query);
        return true;
    } catch (error) {
        console.trace(error);
        return false;
    }
}