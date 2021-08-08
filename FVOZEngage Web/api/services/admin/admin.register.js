const db_conn = require('../../database/db');

module.exports = async(email, name, password, profile) => {
    try {
        const query = `INSERT INTO admin 
                        (
                            admin_id, 
                            admin_email, 
                            admin_name, 
                            admin_password,
                            admin_profile
                        ) 
                        VALUES 
                        (
                            null, 
                            '${email}', 
                            '${name}', 
                            '${password}',
                            '${profile}'
                        )
                        `;
        await db_conn(query);
        return true;
    } catch (error) {
        console.trace(error);
        return false;
    }
}