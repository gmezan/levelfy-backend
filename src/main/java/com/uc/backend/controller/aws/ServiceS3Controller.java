package com.uc.backend.controller.aws;

import com.uc.backend.dto.FileUploadDto;
import com.uc.backend.entity.Course;
import com.uc.backend.entity.Service;
import com.uc.backend.enums.UniversityName;
import com.uc.backend.repository.CourseRepository;
import com.uc.backend.repository.ServiceRepository;
import com.uc.backend.service.aws.AwsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("s3/service")
public class ServiceS3Controller {

    private final String folder = "services";
    AwsResourceService awsResourceService;
    ServiceRepository serviceRepository;

    @Autowired
    public ServiceS3Controller(AwsResourceService awsResourceService, ServiceRepository serviceRepository) {
        this.awsResourceService = awsResourceService;
        this.serviceRepository = serviceRepository;
    }

    @PostMapping(
            path = "{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<FileUploadDto> uploadServiceImage(@PathVariable("id") int idService,
                                                           @RequestParam("file")MultipartFile file) {

        Optional<Service> serviceOptional = serviceRepository.findById(idService);

        if (!serviceOptional.isPresent()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        Service service = serviceOptional.get();
        try {
            String url = awsResourceService.uploadImage(String.valueOf(service.getIdService()), folder, file);
            service.setPhoto(url);
            serviceRepository.save(service);
            return new ResponseEntity<>(new FileUploadDto(url), HttpStatus.OK);
        } catch (IllegalAccessException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
