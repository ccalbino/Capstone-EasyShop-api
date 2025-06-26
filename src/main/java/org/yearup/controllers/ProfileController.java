package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@CrossOrigin
@PreAuthorize("permitAll()")
public class ProfileController
{
    private UserDao userDao;
    private ProfileDao profileDao;


    @Autowired
    public ProfileController(UserDao userDao,ProfileDao profileDao)
    {
        this.userDao = userDao;
        this.profileDao = profileDao;

    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public Profile getProfile(Principal principal) {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            var profile = profileDao.getProfile(userId);

            return profile;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.PUT)
    public Profile updateProfile(@RequestBody Profile profile, Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            profile.setUserId(userId);
            profileDao.editProfile(profile);

            return profileDao.getProfile(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
