package org.alixar.daw2.dario.dwese_ticket_logger_webapp.dwese_ticket_logger_webapp.controllers;

import jakarta.validation.Valid;
import org.alixar.daw2.dario.dwese_ticket_logger_webapp.dwese_ticket_logger_webapp.entities.Region;
import org.alixar.daw2.dario.dwese_ticket_logger_webapp.dwese_ticket_logger_webapp.repositories.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/regions")
public class RegionController {

    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    private final RegionRepository regionRepository;
    private final MessageSource messageSource;

    public RegionController(RegionRepository regionRepository, MessageSource messageSource) {
        this.regionRepository = regionRepository;
        this.messageSource = messageSource;
    }

    /**
     * Lista todas las regiones.
     */
    @GetMapping
    public String listRegions(Model model, Locale locale) {
        logger.info("Solicitando la lista de todas las regiones...");
        List<Region> listRegions = null;

        try {
            listRegions = regionRepository.findAll();
            logger.info("Se han cargado {} regiones.", listRegions.size());
        } catch (DataAccessException e) {
            logger.error("Error al listar las regiones: {}", e.getMessage());
            model.addAttribute(
                    "errorMessage",
                    messageSource.getMessage(
                            "msg.region-controller.list.error",
                            null,
                            "Error al listar las regiones.",
                            locale
                    )
            );
        }

        model.addAttribute("listRegions", listRegions);
        return "regions/region";
    }

    /**
     * Muestra el formulario para crear una región.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva región.");
        model.addAttribute("region", new Region());
        return "regions/region-form";
    }

    /**
     * Muestra el formulario para editar una región.
     */
    @GetMapping("/edit")
    public String showEditForm(
            @RequestParam("id") Long id,
            Model model,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Mostrando formulario de edición para la región con ID {}", id);

        try {
            Optional<Region> regionOpt = regionRepository.findById(id);

            if (regionOpt.isEmpty()) {
                logger.warn("No se encontró la región con ID {}", id);
                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        "No se encontró la región."
                );
                return "redirect:/regions";
            }

            model.addAttribute("region", regionOpt.get());

        } catch (DataAccessException e) {
            logger.error("Error al obtener la región con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    messageSource.getMessage(
                            "msg.region-controller.edit.error",
                            null,
                            "Error al obtener la región.",
                            locale
                    )
            );
            return "redirect:/regions";
        }

        return "regions/region-form";
    }

    /**
     * Inserta una nueva región.
     */
    @PostMapping("/insert")
    public String insertRegion(
            @Valid @ModelAttribute("region") Region region,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Insertando nueva región con código {}", region.getCode());

        try {
            if (result.hasErrors()) {
                return "regions/region-form";
            }

            if (regionRepository.existsRegionByCode(region.getCode())) {
                logger.warn("El código de la región {} ya existe.", region.getCode());
                result.rejectValue(
                        "code",
                        "msg.region-controller.insert.codeExist"
                );
                return "regions/region-form";
            }

            regionRepository.save(region);
            logger.info("Región {} insertada con éxito.", region.getCode());

        } catch (DataAccessException e) {
            logger.error("Error al insertar la región {}: {}", region.getCode(), e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    messageSource.getMessage(
                            "msg.region-controller.insert.error",
                            null,
                            locale
                    )
            );
        }

        return "redirect:/regions";
    }

    /**
     * Actualiza una región existente.
     */
    @PostMapping("/update")
    public String updateRegion(
            @Valid @ModelAttribute("region") Region region,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Actualizando región con ID {}", region.getId());

        try {
            if (result.hasErrors()) {
                return "regions/region-form";
            }

            if (regionRepository.existsRegionByCodeAndNotId(
                    region.getCode(),
                    region.getId())) {

                logger.warn(
                        "El código de la región {} ya existe para otra región.",
                        region.getCode()
                );
                result.rejectValue(
                        "code",
                        "msg.region-controller.update.codeExist"
                );
                return "regions/region-form";
            }

            regionRepository.save(region);
            logger.info("Región con ID {} actualizada con éxito.", region.getId());

        } catch (DataAccessException e) {
            logger.error("Error al actualizar la región con ID {}: {}", region.getId(), e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    messageSource.getMessage(
                            "msg.region-controller.update.error",
                            null,
                            locale
                    )
            );
        }

        return "redirect:/regions";
    }

    /**
     * Elimina una región.
     */
    @PostMapping("/delete")
    public String deleteRegion(
            @RequestParam("id") Long id,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Eliminando región con ID {}", id);

        try {
            regionRepository.deleteById(id);
            logger.info("Región con ID {} eliminada con éxito.", id);
        } catch (DataAccessException e) {
            logger.error("Error al eliminar la región con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    messageSource.getMessage(
                            "msg.region-controller.delete.error",
                            null,
                            "Error al eliminar la región.",
                            locale
                    )
            );
        }

        return "redirect:/regions";
    }
}