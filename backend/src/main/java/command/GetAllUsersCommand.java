package command;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.responseDto.UserResponseDTO;
import model.User;
import service.UserService;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllUsersCommand implements Command {

    private final ObjectMapper mapper = new ObjectMapper();
    private final UserService userService;

    public GetAllUsersCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(Object data, ClientContext context) {
        try {
            // Fetch all users from service
            List<User> users = userService.getAllUsers();

            // Convert each User to UserResponseDTO
            List<UserResponseDTO> dtoList = users.stream()
                    .map(u -> new UserResponseDTO(
                            u.getUserId(),
                            u.getUsername(),
                            u.getEmail(),
                            u.getRole(),
                            u.getContactNumber(),
                            u.getProfilePicture()
                    ))
                    .collect(Collectors.toList());

            // Convert DTO list to JSON
            String jsonResponse = mapper.writeValueAsString(dtoList);

            // Send to frontend
            context.getOutput().println(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            context.getOutput().println("{\"success\":false,\"message\":\"Server error\"}");
        }
    }
}