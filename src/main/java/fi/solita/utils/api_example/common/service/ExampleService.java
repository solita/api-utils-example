package fi.solita.utils.api_example.common.service;

import static fi.solita.utils.functional.Collections.newList;

import java.util.List;

import org.springframework.stereotype.Service;

import fi.solita.utils.api.Includes;
import fi.solita.utils.api.types.Count;
import fi.solita.utils.api_example.versioned.v0_1.dto.ExampleDto;

@Service
public class ExampleService {

    public static List<Long> revisions = newList(1l);

    public List<ExampleDto> examples(Count count, Includes<ExampleDto> includes) {
        return null;
    }

    public ExampleDto example(long id, Includes<ExampleDto> includes) {
        return null;
    }
}
