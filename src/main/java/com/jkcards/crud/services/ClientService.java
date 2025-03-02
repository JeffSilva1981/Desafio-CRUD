package com.jkcards.crud.services;

import com.jkcards.crud.dto.ClientDto;
import com.jkcards.crud.entities.Client;
import com.jkcards.crud.repositories.ClientRepository;
import com.jkcards.crud.services.exceptions.DatabaseException;
import com.jkcards.crud.services.exceptions.ResourseNotFoundException;
import com.jkcards.crud.services.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public ClientDto findById(Long id){
        Client result = clientRepository.findById(id).orElseThrow(()->
                new ResourseNotFoundException("Recurso não encontrado"));
       return new ClientDto(result);

    }

    @Transactional(readOnly = true)
    public Page<ClientDto> findAll(Pageable pageable){
        Page<Client> result = clientRepository.findAll(pageable);
      return result.map(ClientDto::new);
    }
    @Transactional
    public ClientDto insert(ClientDto dto){

        try {
            validateClient(dto);
            Client entity = new Client();
            copyDtoToEntity(dto, entity);
            entity = clientRepository.save(entity);
            return new ClientDto(entity);

        } catch (DataIntegrityViolationException e) {
                throw new ValidationException("Dados Inválidos");
            }
    }

    @Transactional
    public ClientDto update(Long id, ClientDto dto){

        if (!clientRepository.existsById(id)){
            throw new ResourseNotFoundException("Recurso não encontrado");
        }

        validateClient(dto);

        try {
            Client entity = clientRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = clientRepository.save(entity);
            return new ClientDto(entity);
        }
        catch (DataIntegrityViolationException e){
            throw new ValidationException("Dados Inválidos");
        }

    }
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        if (!clientRepository.existsById(id)){
            throw new ResourseNotFoundException("Recurso não encontrado");
        }try {
            clientRepository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new DatabaseException("Falha de integridade referêncial");
        }

    }

    private void copyDtoToEntity(ClientDto dto, Client entity) {

        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
    }
    private void validateClient(ClientDto dto) {

        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new ValidationException("Dados Inválidos.");
        }
        if (dto.getCpf() == null || dto.getCpf().trim().isEmpty()) {
            throw new ValidationException("Dados Inválidos.");
        }
        if (dto.getIncome() == null || dto.getIncome() < 0) {
            throw new ValidationException("Dados Inválidos.");
        }
        if (dto.getBirthDate() == null) {
            throw new ValidationException("Dados Inválidos.");
        }
        if (dto.getChildren() == null || dto.getChildren() < 0) {
            throw new ValidationException("Dados Inválidos.");
        }

    }

}
