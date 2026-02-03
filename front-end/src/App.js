import './App.css';
import React, { useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './component/common/Navbar';
import FooterComponent from './component/common/Footer';
import HomePage from './component/home/HomePage';
import AllRoomsPage from './component/booking_rooms/AllRoomsPage';
import FindBookingPage from './component/booking_rooms/FindBookingPage';
import RoomDetailsPage from './component/booking_rooms/RoomDetailsPage';
import LoginPage from './component/auth/LoginPage';
import RegisterPage from './component/auth/RegisterPage';
import ProfilePage from './component/profile/ProfilePage';
import EditProfilePage from './component/profile/EditProfilePage';
import { ProtectedRoute, AdminRoute } from './service/guard';
import AdminPage from './component/admin/AdminPage';
import ManageRoomPage from './component/admin/ManageRoomPage';
import ManageBookingsPage from './component/admin/ManageBookingsPage';
import AddRoomPage from './component/admin/AddRoomPage';
import EditRoomPage from './component/admin/EditRoomPage';
import EditBookingPage from './component/admin/EditBookingPage';

function App() {
    useEffect(() => {
        const docTitle = document.title;
    
        const handleBlur = () => {
          document.title = "Come back 🥺";
        };
    
        const handleFocus = () => {
          document.title = docTitle;
        };
    
        window.addEventListener("blur", handleBlur);
        window.addEventListener("focus", handleFocus);
    
        // Cleanup the event listeners on component unmount
        return () => {
          window.removeEventListener("blur", handleBlur);
          window.removeEventListener("focus", handleFocus);
        };
    }, []);

    return (
        <BrowserRouter>
            <div className="App">
                <Navbar />

                <div className='content'>
                    <Routes>
                        <Route exact path='/home' element={<HomePage />} />
                        <Route exact path='/rooms' element={<AllRoomsPage />} />
                        <Route path='/find-booking' element={<FindBookingPage />} />
                        <Route path='/login' element={<LoginPage />} />
                        <Route path='/register' element={<RegisterPage />} />

                        <Route path='/room-details-book/:roomId' element={<ProtectedRoute element={<RoomDetailsPage />} /> }/>
                        <Route path='/profile' element={<ProfilePage />} />
                        <Route path='/edit-profile' element={<EditProfilePage />} />

                        <Route path='/admin' element={<AdminRoute element={<AdminPage />} />} />
                        <Route path='/admin/manage-rooms' element={<AdminRoute element={<ManageRoomPage />} />} />
                        <Route path='/admin/manage-bookings' element={<AdminRoute element={<ManageBookingsPage />} />} />
                        <Route path='/admin/add-room' element={<AdminRoute element={<AddRoomPage />} />} />
                        <Route path='/admin/edit-room/:roomId' element={<ProtectedRoute element={<EditRoomPage />} /> } />
                        <Route path='/admin/edit-booking/:bookingCode' element={<ProtectedRoute element={<EditBookingPage />} /> } />

                        <Route path='*' element={<Navigate to="/home" />} />
                    </Routes>
                </div>

                <FooterComponent />
            </div>
        </BrowserRouter>
    );
}

export default App;
