/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package com.example.BankEmitentService.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * Controller for handling web requests in the application.
 * <p>
 * This class is responsible for routing and returning the appropriate views
 * for various URL endpoints.
 * */
@Controller
public class WebController {

    /**
     * Handles GET requests to the URL "/search".
     *
     * @param model the model object used for passing data to the view
     * @return the name of the view "search" to be displayed to the user
     */
    @GetMapping("/search")
    public String search(Model model) {
        return "search";
    }
}
