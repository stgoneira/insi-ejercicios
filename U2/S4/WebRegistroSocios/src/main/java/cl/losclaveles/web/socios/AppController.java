package cl.losclaveles.web.socios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AppController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public AppController(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @GetMapping("/")
    String formRegistro(
            Model model,
            @ModelAttribute NuevoSocio nuevoSocio)
    {
        model.addAttribute("socio", nuevoSocio);
        return "registro";
    }

    @PostMapping("/registro")
    String procesarRegistroNuevoSocio(
            Model model,
            @ModelAttribute NuevoSocio nuevoSocio
    ) {
        LOGGER.info("Nuevo Socio: {}", nuevoSocio);
        model.addAttribute("socio", nuevoSocio);
        applicationEventPublisher.publishEvent(nuevoSocio);
        return "gracias";
    }
}
