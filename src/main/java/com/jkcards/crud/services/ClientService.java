package com.jkcards.crud.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jkcards.crud.dto.ClientDto;
import com.jkcards.crud.entities.Client;
import com.jkcards.crud.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public ClientDto findById(Long id){
        Optional<Client> result = clientRepository.findById(id);
        Client client = result.get();
        ClientDto dto = new ClientDto(client);
        return dto;
    }

    @Transactional(readOnly = true)
    public Page<ClientDto> findAll(Pageable pageable){
        Page<Client> result = clientRepository.findAll(pageable);
      return result.map(ClientDto::new);
    }
    @Transactional
    public ClientDto insert(ClientDto dto){

        Client entity = new Client();

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());

        entity = clientRepository.save(entity);

        return new ClientDto(entity);

    }

}
