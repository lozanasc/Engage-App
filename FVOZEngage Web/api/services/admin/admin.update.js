const db_conn = require('../../database/db');
module.exports = {
    change_email: async(old_email, new_email) => {
        try {
            const Query = `UPDATE admin SET admin_email = '${new_email}' WHERE admin_email = '${old_email}'`;
            await db_conn(Query);
            return true;
        } catch (error) {
            console.trace(error);
            return false;
        }
    },
    change_name: async(old_name, new_name) => {
        try {
            const Query = `UPDATE admin SET admin_name = '${new_name}' WHERE admin_name = '${old_name}'`;
            await db_conn(Query);
            return true;
        } catch (error) {
            console.trace(error);
            return false;
        }
    },
    change_password: async(new_password, curr_email) => {
        try {
            const Query = `UPDATE admin SET admin_password = '${new_password}' WHERE admin_email = '${curr_email}'`;
            await db_conn(Query);
            return true;
        } catch (error) {
            return false;
        }
    },
    delete_acc: async(curr_id) => {
        try {
            await db_conn(`DELETE FROM admin WHERE admin_id = ${curr_id}`);
            return true;
        } catch (error) {
            return false;
        }
    }
}