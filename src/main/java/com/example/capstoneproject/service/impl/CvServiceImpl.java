package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.CertificationMapper;
import com.example.capstoneproject.mapper.ContactMapper;
import com.example.capstoneproject.mapper.CvMapper;
import com.example.capstoneproject.mapper.EducationMapper;
import com.example.capstoneproject.repository.ContactRepository;
import com.example.capstoneproject.repository.CustomerRepository;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.TemplateRepository;
import com.example.capstoneproject.service.ContactService;
import com.example.capstoneproject.service.CvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class CvServiceImpl extends AbstractBaseService<Cv, CvDto, Integer> implements CvService {
    @Autowired
    CvRepository cvRepository;

    @Autowired
    CvMapper cvMapper;

    @Autowired
    CertificationMapper certificationMapper;

    @Autowired
    EducationMapper educationMapper;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    TemplateRepository templateRepository;

    public CvServiceImpl(CvRepository cvRepository, CvMapper cvMapper) {
        super(cvRepository, cvMapper, cvRepository::findById);
        this.cvRepository = cvRepository;
        this.cvMapper = cvMapper;
    }


    @Override
    public List<CvDto> GetCvsById(int customerId) {
        return cvRepository.findAllByCustomerIdAndStatus(customerId, CvStatus.ACTIVE)
                .stream()
                .map(cv -> {
                    CvDto cvDto = new CvDto();
                    cvDto.setId(cv.getId());
                    cvDto.setContent(cv.getContent());
                    cvDto.setSummary(cv.getSummary());
                    Customer customer = cv.getCustomer();
                    if (customer != null) {
                        CustomerViewDto customerViewDto = new CustomerViewDto();
                        customerViewDto.setId(customer.getId());
                        customerViewDto.setName(customer.getName());
                        cvDto.setCustomer(customerViewDto);
                    }
                    Template template = cv.getTemplate();
                    if (template != null) {
                        TemplateViewDto templateViewDto = new TemplateViewDto();
                        templateViewDto.setId(template.getId());
                        templateViewDto.setName(template.getName());
                        templateViewDto.setContent(template.getContent());
                        templateViewDto.setAmountView(template.getAmountView());
                        cvDto.setTemplate(templateViewDto);
                    }
                    Contact contact = cv.getContact();
                    if (contact != null) {
                        ContactViewDto contactDto = new ContactViewDto();
                        contactDto.setId(contact.getId());
                        contactDto.setState(contact.getState());
                        contactDto.setCountry(contact.getCountry());
                        contactDto.setFullName(contact.getFullName());
                        contactDto.setWebsite(contact.getWebsite());
                        contactDto.setPhone(contact.getPhone());
                        contactDto.setLinkin(contact.getLinkin());
                        contactDto.setEmail(contact.getEmail());
                        cvDto.setContact(contactDto);
                    }
//                    cvDto.setCertifications(
//                            cv.getCertifications().stream()
//                                    .filter(certification -> certification.getStatus() == CvStatus.ACTIVE)
//                                    .map(certification -> {
//                                        CertificationViewDto certificationViewDto = new CertificationViewDto();
//                                        certificationViewDto.setId(certification.getId());
//                                        certificationViewDto.setName(certification.getName());
//                                        certificationViewDto.setCertificateSource(certification.getCertificateSource());
//                                        certificationViewDto.setEndYear(certification.getEndYear());
//                                        certificationViewDto.setCertificateRelevance(certification.getCertificateRelevance());
//                                        return certificationViewDto;
//                                    })
//                                    .collect(Collectors.toList())
//                    );
                    return cvDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CvDto GetCvsByCvId(int customerId, int cvId) {
        Cv cv = cvRepository.findCvByIdAndStatus(customerId, cvId, CvStatus.ACTIVE);

        if (cv != null) {
            CvDto cvDto = new CvDto();
            cvDto.setId(cv.getId());
            cvDto.setContent(cv.getContent());
            cvDto.setSummary(cv.getSummary());
            Customer customer = cv.getCustomer();
            if (customer != null) {
                CustomerViewDto customerViewDto = new CustomerViewDto();
                customerViewDto.setId(customer.getId());
                customerViewDto.setName(customer.getName());
                cvDto.setCustomer(customerViewDto);
            }
            Template template = cv.getTemplate();
            if (template != null) {
                TemplateViewDto templateViewDto = new TemplateViewDto();
                templateViewDto.setId(template.getId());
                templateViewDto.setName(template.getName());
                templateViewDto.setContent(template.getContent());
                templateViewDto.setAmountView(template.getAmountView());
                cvDto.setTemplate(templateViewDto);
            }
            Contact contact = cv.getContact();
            if (contact != null) {
                ContactViewDto contactDto = new ContactViewDto();
                contactDto.setId(contact.getId());
                contactDto.setState(contact.getState());
                contactDto.setCountry(contact.getCountry());
                contactDto.setFullName(contact.getFullName());
                contactDto.setWebsite(contact.getWebsite());
                contactDto.setPhone(contact.getPhone());
                contactDto.setLinkin(contact.getLinkin());
                contactDto.setEmail(contact.getEmail());
                cvDto.setContact(contactDto);
            }

            return cvDto;
        } else {
            throw new IllegalArgumentException("CV not found with cvId: " + cvId);
        }
    }

    @Override
    public void deleteCvById(Integer customerId, Integer id) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            Optional<Cv> cvOptional = cvRepository.findByIdAndCustomerId(id, customerId);

            if (cvOptional.isPresent()) {
                Cv cv = cvOptional.get();
                cv.setStatus(CvStatus.DELETED);
                cvRepository.save(cv);
            } else {
                throw new IllegalArgumentException("CV not found with id: " + id);
            }
        } else {
            throw new IllegalArgumentException("Customer not found with id: " + customerId);
        }
    }


    @Override
    public CvAddNewDto createCv(Integer customerId, CvAddNewDto dto) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            Cv cv = new Cv();
            cv.setContent(dto.getContent());
            cv.setStatus(CvStatus.ACTIVE);
            Customer customer = customerOptional.get();
            cv.setCustomer(customer);
            Cv savedCv = cvRepository.save(cv);
            CvAddNewDto createdDto = new CvAddNewDto();
            createdDto.setContent(savedCv.getContent());

            return createdDto;
        } else {
            throw new IllegalArgumentException("Không tìm thấy khách hàng với id: " + customerId);
        }
    }


    @Override
    public Cv getCvById(int cvId) {
        Optional<Cv> cvOptional = cvRepository.findById(cvId);
        if (cvOptional.isPresent()) {
            return cvOptional.get();
        } else {
            throw new IllegalArgumentException("CV not found with ID: " + cvId);
        }
    }

    @Override
    public boolean updateCvSummary(int customerId, int cvId, CvUpdateSumDto dto) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            Optional<Cv> cvOptional = cvRepository.findById(cvId);

            if (cvOptional.isPresent()) {
                Cv cv = cvOptional.get();
                cv.setSummary(dto.getSummary());

                cvRepository.save(cv);

                return true;
            } else {
                throw new IllegalArgumentException("CvId not found: " + cvId);
            }
        } else {
            throw new IllegalArgumentException("CustomerId not found: " + customerId);
        }
    }

    @Override
    public boolean updateCvContent(int customerId, int cvId, CvAddNewDto dto) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            Optional<Cv> cvOptional = cvRepository.findById(cvId);

            if (cvOptional.isPresent()) {
                Cv cv = cvOptional.get();
                cv.setContent(dto.getContent());

                cvRepository.save(cv);

                return true;
            } else {
                throw new IllegalArgumentException("CvId not found: " + cvId);
            }
        } else {
            throw new IllegalArgumentException("CustomerId not found: " + customerId);
        }
    }

    @Override
    public boolean updateCvContact(int customerId, int cvId, int contactId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            Optional<Cv> cvOptional = cvRepository.findById(cvId);

            if (cvOptional.isPresent()) {
                Cv cv = cvOptional.get();
                Optional<Contact> contactOptional = contactRepository.findById(contactId);

                if (contactOptional.isPresent()) {
                    Contact contact = contactOptional.get();
                    cv.setContact(contact);

                    cvRepository.save(cv);

                    return true;
                } else {
                    throw new IllegalArgumentException("ContactId not found: " + contactId);
                }
            } else {
                throw new IllegalArgumentException("CvId not found: " + cvId);
            }
        } else {
            throw new IllegalArgumentException("CustomerId not found: " + customerId);
        }
    }

    @Override
    public boolean updateCvTemplate(int customerId, int cvId, int templateId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            Optional<Cv> cvOptional = cvRepository.findById(cvId);

            if (cvOptional.isPresent()) {
                Cv cv = cvOptional.get();
                Optional<Template> templateOptional = templateRepository.findById(templateId);

                if (templateOptional.isPresent()) {
                    Template template = templateOptional.get();
                    cv.setTemplate(template);

                    cvRepository.save(cv);

                    return true;
                } else {
                    throw new IllegalArgumentException("TemplateId not found: " + templateId);
                }
            } else {
                throw new IllegalArgumentException("CvId not found: " + cvId);
            }
        } else {
            throw new IllegalArgumentException("CustomerId not found: " + customerId);
        }
    }



}
