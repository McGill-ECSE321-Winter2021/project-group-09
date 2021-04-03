let x = false
if ('PROD_BACKEND' in process.env) {
    x = true
}
export const BACKEND = x ? "https://repairshop-backend-ecse321-09.herokuapp.com:5432" : "http://localhost:8080"
export const HEROKU_BACKEND = "https://repairshop-backend-ecse321-09.herokuapp.com"
export const LOGIN_ENDPOINT = "/api/authentication/login"
export const REGISTER_CUSTOMER_ENDPOINT = "/api/customer/register"
export const REGISTER_ADMIN_ENDPOINT = "/api/admin/register"
export const REGISTER_TECHNICIAN_ENDPOINT = "/api/technician/register"
export const ALL_SERVICES_ENDPOINT = "/api/service/all";
export const POSSIBLE_APPOINTMENTS_ENDPOINT = "/api/appointment/possibilities";
export const CREATE_APPOINTMENT_ENDPOINT = "/api/appointment/create";
export const LOGOUT_ENDPOINT = "/api/authentication/logout";
export const SERVICE_ENDPOINT = "/api/service";
export const ALL_HOLIDAYS_ENDPOINT = "/api/business/holidays";
export const DELETE_HOLIDAY_ENDPOINT = "/api/business/delete/holidays";
export const CHANGE_PASS_TECH = "/api/technician/changePassword/"
export const CHANGE_PASS_ADMIN = "/api/admin/changePassword/"
export const CHANGE_PASS_CUS = "/api/customer/changePassword/"
export const CANCEL_APPOINTMENT_ENDPOINT = "/api/appointment/cancel/"
export const TECHNICIAN_ENDPOINT = "/api/technician/";
export const GET_BUSINESS_ENDPOINT = "/api/business/info/"
