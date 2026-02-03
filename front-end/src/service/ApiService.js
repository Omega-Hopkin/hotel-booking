import axios from 'axios';

export default class ApiService {
    static BASE_URL = 'http://localhost:8080';

    static getHeader() {
        const token = localStorage.getItem('token');

        return {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };
    }

    /* AUTH */
    static async registerUser(registration){
        const response = await axios.post(`${this.BASE_URL}/auth/register`, registration)
        return response.data;
    }

    /* login registrated user */
    static async loginUser(loginDetails){
        const response = await axios.post(`${this.BASE_URL}/auth/login`, loginDetails)
        return response.data;
    }

    /* USERS */
    static async getAllUsers() {
        const response = await axios.get(`${this.BASE_URL}/users/all`, {
            headers: this.getHeader()
        })
        return response.data;
    }

    static async getUserProfile(){
        const response = await axios.get(`${this.BASE_URL}/users/get-logged-in-profile-info`, {
            headers: this.getHeader()
        })
        return response.data;
    }

    /* This is to get a single user */
    static async getUser(userId) {
        const response = await axios.get(`${this.BASE_URL}/users/get-by-id/${userId}`, {
            headers: this.getHeader()
        })
        return response.data;
    }

    /* This is to get user bookings by the user id */
    static async getUserBookings(userId) {
        const response = await axios.get(`${this.BASE_URL}/users/get-user-bookings/${userId}`, {
            headers: this.getHeader()
        })
        return response.data;
    }

    /* This is to delete a user */
    static async deleteUser(userId) {
        const response = await axios.delete(`${this.BASE_URL}/users/delete/${userId}`, {
            headers: this.getHeader()
        })
        return response.data;
    } 

    /* ROOM */
    /* adds new room to database */
    static async addRoom(formData) {
        const result = await axios.post(`${this.BASE_URL}/rooms/add`, formData, {
            headers: {
                ...this.getHeader(),
                'Content-Type': 'multipart/form-data'
            }
        });

        return result.data;
    }

    /* This is to get all available rooms */
    static async getAllAvailableRooms() {
        const result = await axios.get(`${this.BASE_URL}/rooms/all-available-rooms`)
        return result.data;
    }   

    /* This is to get available rooms by dates from database with a given date and a romm type */
    static async getAvailableRoomsByDateAndType(checkInDate, checkOutDate, roomType) {
        const result = await axios.get(`${this.BASE_URL}/rooms/available-rooms-by-date-and-type?checkInDate=${checkInDate}&checkOutDate=${checkOutDate}&roomType=${roomType}`)
        return result.data;
    }

    /* This gets all room type from database */
    static async getRoomTypes() {
        const response = await axios.get(`${this.BASE_URL}/rooms/types`)
        return response.data;
    }

    static async getAllRooms() {
        const result = await axios.get(`${this.BASE_URL}/rooms/all`)
        return result.data;
    }

    /* This is to get a room  by id */
    static async getRoomById(roomId) {
        const result = await axios.get(`${this.BASE_URL}/rooms/room-by-id/${roomId}`)
        return result.data;
    }

    /* This is to delete a room */
    static async deleteRoom(roomId) {
        const result = await axios.delete(`${this.BASE_URL}/rooms/delete/${roomId}`, {
            headers: this.getHeader()
        })
        return result.data;
    }

    /* This update a room */
    static async updateRoom(roomId, formData) {
        const result = await axios.put(`${this.BASE_URL}/rooms/update/${roomId}`, formData, {
            headers: {
                ...this.getHeader(),
                'Content-Type': 'multipart/form-data'
            }
        });
        return result.data;
    }

    /* BOOKING */
    /* This saves a new booking in the db */
    static async bookRoom(roomId, userId, booking) {
        console.log("USER ID IS: " + userId)

        const response = await axios.post(`${this.BASE_URL}/bookings/book-room/${roomId}/${userId}`, booking, {
            headers: this.getHeader()
        })
        return response.data;
    }

    /* This gets all bookings from the db */
    static async getAllBookings() {
        const response = await axios.get(`${this.BASE_URL}/bookings/all`, {
            headers: this.getHeader()
        })
        return response.data;
    }

    /* This gets bookings by confirmation code */
    static async getBookingByConfirmationCode(bookingCode) {
        const response = await axios.get(`${this.BASE_URL}/bookings/get-by-confirmation-code/${bookingCode}`);
        return response.data;
    }

    /* This is to cancel user booking */
    static async cancelBooking(bookingId) {
        const response = await axios.delete(`${this.BASE_URL}/bookings/cancel/${bookingId}`, {
            headers: this.getHeader()
        })
        return response.data;
    }

    /* AUTHENTICATION CHECKER */
    static logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
    }

    static isAuthenticated() {
        const token = localStorage.getItem('token');
        return !!token;
    }

    static isAdmin() {
        const role = localStorage.getItem('role');
        return role === 'ADMIN';
    }

    static isUser() {
        const role = localStorage.getItem('role');
        return role === 'USER';
    }
}
// export default new ApiService();