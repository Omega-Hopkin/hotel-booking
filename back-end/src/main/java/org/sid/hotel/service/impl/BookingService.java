package org.sid.hotel.service.impl;

import java.util.List;

import org.sid.hotel.dto.BookingDTO;
import org.sid.hotel.dto.Response;
import org.sid.hotel.entity.Booking;
import org.sid.hotel.entity.Room;
import org.sid.hotel.entity.User;
import org.sid.hotel.exception.OurException;
import org.sid.hotel.repo.BookingRepository;
import org.sid.hotel.repo.RoomRepository;
import org.sid.hotel.repo.UserRepository;
import org.sid.hotel.service.interfaces.IBookingService;
// import org.sid.hotel.service.interfaces.IRoomService;
import org.sid.hotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class BookingService implements IBookingService {
    @Autowired
    private BookingRepository bookingRepository;

    // @Autowired
    // private IRoomService roomService;

    @Autowired 
    private RoomRepository roomRepository;

    @Autowired 
    private UserRepository userRepository;


    // private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
    //     return existingBookings.stream()
    //             .noneMatch(existingBookings -> 
    //                 bookingRequest.getCheckInDate().equals(existingBookings.getCheckInDate()) 
    //                 || bookingRequest.getCheckOutDate().isBefore(existingBookings.getCheckOutDate()) 
    //                 || (bookingRequest.getCheckInDate().isAfter(existingBookings.getCheckInDate()) 
    //                 && bookingRequest.getCheckInDate().isBefore(existingBookings.getCheckOutDate()))
    //                 || bookingRequest.getCheckInDate().isBefore(existingBookings.getCheckInDate())

    //                 && bookingRequest.getCheckOutDate().equals(existingBookings.getCheckOutDate()))
    //                 || (bookingRequest.getCheckInDate().isBefore(existingBookings.getCheckInDate())

    //                 && bookingRequest.getCheckOutDate().isAfter(existingBookings.getCheckOutDate()))

    //                 || (bookingRequest.getCheckInDate().equals(existingBookings.getCheckOutDate())
    //                 && bookingRequest.getCheckOutDate().equals(existingBookings.getCheckOutDate()))

    //                 || (bookingRequest.getCheckInDate().equals(existingBookings.getCheckOutDate())
    //                 && bookingRequest.getCheckOutDate().equals(existingBookings.getCheckInDate()))
    //             );
    // }

    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckInDate())));
    }
    

    @Transactional
    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        Response response = new Response();

        try{
            if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
                throw new IllegalArgumentException("Check in date must come after check out date");
            }

            Room room = roomRepository.findById(roomId).orElseThrow(()-> new OurException("Room Not Found"));
            User user = userRepository.findById(userId).orElseThrow(()-> new OurException("User Not Found"));

            List<Booking> existingBookings = room.getBookings();
            if(!roomIsAvailable(bookingRequest, existingBookings)){
                throw new OurException("Room is not available for selected date range");
            }

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);
            response.setStatusCode(200);
            response.setMessage("Booking created successfully");
            response.setBookingConfirmationCode(bookingConfirmationCode);
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a booking " + e.getMessage());
        }

        return response;
    }

    @Override
    @Transactional
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();

        try{
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(()-> new OurException("Booking Not Found"));
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRoom(booking, true);
            response.setStatusCode(200);
            response.setMessage("successfull");
            response.setBooking(bookingDTO);
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error finding a booking " + e.getMessage());
        }

        return response;
    }

    @Override 
    public Response getAllBookings() {
        Response response = new Response();

        try{
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setStatusCode(200);
            response.setMessage("successfull");
            response.setBookingList(bookingDTOList);
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all booking " + e.getMessage());
        }

        return response;
    }

    @Override 
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();

        try{
            bookingRepository.findById(bookingId).orElseThrow(()-> new OurException("Booking Not Found"));
            bookingRepository.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("successfull");
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error cancelling a booking " + e.getMessage());
        }

        return response;
    }
}
