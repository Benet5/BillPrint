package Billprint.Auth.User;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class WhitelistServiceTest {

    @Test
    void ShouldCreateAndCheckAnItem(){
        WhitelistRepo whitelistRepo = mock(WhitelistRepo.class);
        WhitelistService whitelistService = new WhitelistService(whitelistRepo);

        Whitelist toSave = new Whitelist();
        toSave.setEmail("TestKaiser@nf.de");
        Whitelist saved = new Whitelist("55555","TestKaiser@nf.de" );

        when(whitelistRepo.save(toSave)).thenReturn(saved);

        whitelistService.createItem("TestKaiser@nf.de");
        verify(whitelistRepo).save(toSave);

        when(whitelistRepo.findByEmail("TestKaiser@nf.de")).thenReturn(Optional.of(saved));
        var actual = whitelistService.check("TestKaiser@nf.de");
        assertTrue(actual);
    }

    @Test

    void ShouldFindAllAndDeleteOne(){
        WhitelistRepo whitelistRepo = mock(WhitelistRepo.class);
        WhitelistService whitelistService = new WhitelistService(whitelistRepo);
        Whitelist saved1 = new Whitelist("55555","TestKaiser@nf.de" );
        Whitelist saved2 = new Whitelist("1244653567","TestQueen@nf.de" );

        when(whitelistRepo.findAll()).thenReturn(List.of(saved1, saved2));
        var actual = whitelistService.findAll();
         assertEquals(2, actual.size());
         assertEquals(actual.get(1).getEmail(), "TestQueen@nf.de");

         whitelistService.deleteByEmail("TestQueen@nf.de");
         verify(whitelistRepo).deleteByEmail("TestQueen@nf.de");

    }


}