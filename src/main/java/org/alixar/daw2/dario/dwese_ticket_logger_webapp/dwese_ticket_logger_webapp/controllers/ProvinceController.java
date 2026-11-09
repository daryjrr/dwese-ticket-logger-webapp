package org.alixar.daw2.dario.dwese_ticket_logger_webapp.dwese_ticket_logger_webapp.controllers;

import jakarta.validation.Valid;
import org.alixar.daw2.dario.dwese_ticket_logger_webapp.dwese_ticket_logger_webapp.entities.Province;
import org.alixar.daw2.dario.dwese_ticket_logger_webapp.dwese_ticket_logger_webapp.entities.Region;
import org.alixar.daw2.dario.dwese_ticket_logger_webapp.dwese_ticket_logger_webapp.repositories.ProvinceRepository;
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
@RequestMapping("/provinces")
public class ProvinceController {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);

    private final ProvinceRepository provinceRepository;
    private final RegionRepository regionRepository;
    private final MessageSource messageSource;

    public ProvinceController(ProvinceRepository provinceRepository,
                              RegionRepository regionRepository,
                              MessageSource messageSource) {
        this.provinceRepository = provinceRepository;
        this.regionRepository = regionRepository;
        this.messageSource = messageSource;
    }

    /**
     * Lista todas las provincias.
     */
    @GetMapping
    public String listProvinces(Model model, Locale locale) {
        logger.info("Solicitando la lista de todas las provincias...");
        List<Province> listProvinces = null;

        try {
            listProvinces = provinceRepository.findAll();
            logger.info("Se han cargado {} provincias.", listProvinces.size());
        } catch (DataAccessException e) {
            logger.error("Error al listar las provincias: {}", e.getMessage());
            model.addAttribute("errorMessage",
                    messageSource.getMessage(
                            "msg.province-controller.list.error",
                            null,
                            locale
                    ));
        }

        model.addAttribute("listProvinces", listProvinces);
        return "provinces/province";
    }

    /**
     * Muestra el formulario para crear una provincia.
     */
    @GetMapping("/new")
    public String showNewForm(Model model,
                              RedirectAttributes redirectAttributes,
                              Locale locale) {

        logger.info("Mostrando formulario para nueva provincia.");

        Province province = new Province();
        province.setRegion(new Region());

        model.addAttribute("province", province);
        return loadRegionsAndShowForm(model, redirectAttributes, locale);
    }

    /**
     * Muestra el formulario para editar una provincia.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id,
                               Model model,
                               RedirectAttributes redirectAttributes,
                               Locale locale) {

        logger.info("Mostrando formulario de edición para la provincia con ID {}", id);

        try {
            Optional<Province> provinceOpt = provinceRepository.findById(id);

            if (provinceOpt.isEmpty()) {
                logger.warn("No se encontró la provincia con ID {}", id);
                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        messageSource.getMessage(
                                "msg.province-controller.edit.notFound",
                                null,
                                locale
                        )
                );
                return "redirect:/provinces";
            }

            model.addAttribute("province", provinceOpt.get());

        } catch (DataAccessException e) {
            logger.error("Error al obtener la provincia con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    messageSource.getMessage(
                            "msg.province-controller.edit.error",
                            null,
                            locale
                    )
            );
            return "redirect:/provinces";
        }

        return loadRegionsAndShowForm(model, redirectAttributes, locale);
    }

    /**
     * Inserta una nueva provincia.
     */
    @PostMapping("/insert")
    public String insertProvince(
            @Valid @ModelAttribute("province") Province province,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Insertando nueva provincia con código {}", province.getCode());

        try {
            checkRegionSelected(province, result);

            if (result.hasErrors()) {
                return loadRegionsAndShowForm(model, redirectAttributes, locale);
            }

            if (provinceRepository.existsProvinceByCode(province.getCode())) {
                logger.warn("El código de la provincia {} ya existe.", province.getCode());
                result.rejectValue(
                        "code",
                        "msg.province-controller.insert.codeExist"
                );
                return loadRegionsAndShowForm(model, redirectAttributes, locale);
            }

            provinceRepository.save(province);
            logger.info("Provincia {} insertada con éxito.", province.getCode());

        } catch (DataAccessException e) {
            logger.error("Error al insertar la provincia {}: {}", province.getCode(), e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    messageSource.getMessage(
                            "msg.province-controller.insert.error",
                            null,
                            locale
                    )
            );
        }

        return "redirect:/provinces";
    }

    /**
     * Actualiza una provincia.
     */
    @PostMapping("/update")
    public String updateProvince(
            @Valid @ModelAttribute("province") Province province,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Actualizando provincia con ID {}", province.getId());

        try {
            checkRegionSelected(province, result);

            if (result.hasErrors()) {
                return loadRegionsAndShowForm(model, redirectAttributes, locale);
            }

            if (provinceRepository.existsProvinceByCodeAndNotId(
                    province.getCode(), province.getId())) {

                logger.warn(
                        "El código de la provincia {} ya existe para otra provincia.",
                        province.getCode()
                );
                result.rejectValue(
                        "code",
                        "msg.province-controller.update.codeExist"
                );
                return loadRegionsAndShowForm(model, redirectAttributes, locale);
            }

            provinceRepository.save(province);
            logger.info("Provincia con ID {} actualizada con éxito.", province.getId());

        } catch (DataAccessException e) {
            logger.error("Error al actualizar la provincia con ID {}: {}", province.getId(), e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    messageSource.getMessage(
                            "msg.province-controller.update.error",
                            null,
                            locale
                    )
            );
        }

        return "redirect:/provinces";
    }

    /**
     * Elimina una provincia.
     */
    @PostMapping("/delete")
    public String deleteProvince(
            @RequestParam("id") Long id,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Eliminando provincia con ID {}", id);

        try {
            provinceRepository.deleteById(id);
            logger.info("Provincia con ID {} eliminada con éxito.", id);
        } catch (DataAccessException e) {
            logger.error("Error al eliminar la provincia con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    messageSource.getMessage(
                            "msg.province-controller.delete.error",
                            null,
                            locale
                    )
            );
        }

        return "redirect:/provinces";
    }

    /**
     * Comprueba que se haya seleccionado una región.
     */
    private void checkRegionSelected(Province province, BindingResult result) {
        if (province.getRegion() == null || province.getRegion().getId() == null) {
            result.rejectValue(
                    "region.id",
                    "msg.province.region.notNull"
            );
        }
    }

    /**
     * Carga las regiones y muestra el formulario.
     */
    private String loadRegionsAndShowForm(
            Model model,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        try {
            model.addAttribute("listRegions", regionRepository.findAll());
            return "provinces/province-form";
        } catch (DataAccessException e) {
            logger.error("Error al cargar las regiones para el formulario: {}", e.getMessage());
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    messageSource.getMessage(
                            "msg.province-controller.regions.error",
                            null,
                            locale
                    )
            );
            return "redirect:/provinces";
        }
    }
}