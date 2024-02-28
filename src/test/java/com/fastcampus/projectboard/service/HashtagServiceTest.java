package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Hashtag;
import com.fastcampus.projectboard.repository.HashtagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("비즈니스 로직 - 해시태그")
@ExtendWith(MockitoExtension.class)
class HashtagServiceTest {

    @InjectMocks
    private HashtagService sut;

    @Mock
    private HashtagRepository hashtagRepository;


    @DisplayName("본문을 파싱하면, 해시태그 이름들을 중복 없이 반환한다.")
    @MethodSource //파라마티 테스트 값에 값을 넣어주는 방법 중 하나
    @ParameterizedTest(name ="[{index}] \"{0}\" =>{1}" )    //input을 넣으면 Set<String>의 무언가가 나온다.
    //name 속성 문법에 맞게 잘 써야함.
    void givenContent_whenParsing_thenReturnUniqueHashtagNames(String input, Set<String> expected){
        //given
        //when
        Set<String>  actual=sut.parseHashtagNames(input);
        //then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        then(hashtagRepository).shouldHaveNoInteractions();
    }

    static Stream<Arguments>  givenContent_whenParsing_thenReturnUniqueHashtagNames(){
        return Stream.of(
                arguments("null", Set.of() ),
                arguments("", Set.of() ), arguments("   ", Set.of() ),
                arguments("java", Set.of() ),
                arguments("#  ", Set.of() ),
                arguments("  #", Set.of() ),
                arguments("#java", Set.of("java") ),
                arguments("#java_spring", Set.of("java_spring") ),
                arguments("#java#spring", Set.of("java", "spring") )   //여러상황에 대해서 테스트해주면됨. 이것보다 훨씬 많음 실제로는
        );
    }


    @DisplayName("해시태그 이름들을 입력하면, 저장된 해시태그 중 이름ㅇ르 매칭하는 것들을 중복 없이 반환한다")
    @Test
    void  givenHashtagNames_whenFindingHashtags_thenReturnsHashtagSet(){
        Set<String> hashtagNames= Set.of("java", "spring" , "boots");
        given(hashtagRepository.findByHashtagNameIn(hashtagNames)).willReturn(List.of(
                Hashtag.of("java"),
                Hashtag.of("spring")
        ));

        Set<Hashtag> hashtags=sut.findHashtagsByNames(hashtagNames);

        assertThat(hashtags).hasSize(2);
        then(hashtagRepository).should().findByHashtagNameIn(hashtagNames);
    }


}