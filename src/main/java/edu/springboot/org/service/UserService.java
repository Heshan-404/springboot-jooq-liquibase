package edu.springboot.org.service;

import edu.springboot.org.dto.UserDto;
import org.jooq.DSLContext;
import org.jooq.generated.tables.User;
import org.jooq.generated.tables.records.UserRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final DSLContext dsl;

    public UserService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public UserDto createUser(String name, String email) {
        UserRecord userRecord = dsl.newRecord(User.USER);
        userRecord.setName(name);
        userRecord.setEmail(email);
        userRecord.store();
        return mapRecordToDto(userRecord);
    }

    public UserDto getUserById(Integer id) {
        UserRecord userRecord = dsl.selectFrom(User.USER)
                .where(User.USER.USER_ID.eq(id))
                .fetchOne();
        return userRecord != null ? mapRecordToDto(userRecord) : null;
    }

    public List<UserDto> getAllUsers() {
        List<UserRecord> userRecords = dsl.selectFrom(User.USER)
                .fetch();
        return userRecords.stream()
                .map(this::mapRecordToDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUser(Integer id, String name, String email) {
        UserRecord userRecord = dsl.fetchOne(User.USER, User.USER.USER_ID.eq(id));
        if (userRecord != null) {
            userRecord.setName(name);
            userRecord.setEmail(email);
            userRecord.store();
            return mapRecordToDto(userRecord);
        } else {
            return null;
        }
    }

    public boolean deleteUser(Integer id) {
        int deletedRows = dsl.deleteFrom(User.USER)
                .where(User.USER.USER_ID.eq(id))
                .execute();
        return deletedRows > 0;
    }

    private UserDto mapRecordToDto(UserRecord record) {
        UserDto dto = new UserDto();
        dto.setUserId(record.getUserId());
        dto.setName(record.getName());
        dto.setEmail(record.getEmail());
        return dto;
    }
}