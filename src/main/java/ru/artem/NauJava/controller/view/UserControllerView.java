package ru.artem.NauJava.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.artem.NauJava.entity.Booking;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.model.PaymentStatus;
import ru.artem.NauJava.repository.BookingRepository;
import ru.artem.NauJava.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class UserControllerView
{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @GetMapping("/cabinet")
    public String userListView(Model model, Principal principal)
    {
        String email = principal.getName();
        Optional<User> user = userRepository.findByEmail(email);
        if (email.isEmpty() || user.isEmpty()) {
            return  ":redirect/login";
        }
        List<Booking> bookings = bookingRepository.findBookingsByUserId(user.get().getId());

        model.addAttribute("user", user.get());
        model.addAttribute("bookings", bookings);
        model.addAttribute("payedBookings", bookings.stream().
                filter(b -> b.getPaymentStatus().equals(PaymentStatus.PAYED)).count());
        return "cabinet";
    }
}

