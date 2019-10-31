package fi.solita.utils.api_example.common.service;

import static fi.solita.utils.api.ResponseUtil.redirectToRevision;
import static fi.solita.utils.functional.Collections.newList;
import static fi.solita.utils.functional.Collections.newSortedSet;
import static fi.solita.utils.functional.Functional.head;
import static fi.solita.utils.functional.Option.None;
import static fi.solita.utils.functional.Option.Some;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.solita.utils.api.RequestUtil.ETags;
import fi.solita.utils.api.ResponseUtil;
import fi.solita.utils.api.common.RevisionedSupportServiceBase;
import fi.solita.utils.api.format.SerializationFormat;
import fi.solita.utils.api.types.Revision;
import fi.solita.utils.functional.Option;
import fi.solita.utils.functional.Pair;

@Service
public class SupportService extends RevisionedSupportServiceBase {
    
    @Autowired
    public SupportService() {
        super(Duration.ZERO);
    }
    
    public SortedSet<Long> getRevisionsNewestFirst() {
        return newSortedSet(newList(ExampleService.revisions));
    }
    
    protected Revision getCurrentRevision() {
        return new Revision(head(getRevisionsNewestFirst()));
    }
    
    public void redirectTo(long revision, HttpServletRequest req, HttpServletResponse res) {
        ResponseUtil.cacheFor(revisionsRedirectCached, res);
        redirectToRevision(revision, req, res);
    }
    
    public Option<Pair<SerializationFormat,ETags>> checkRevisionAndUrlAndResolveFormat(LocalDate lahtopaiva, Revision revision, HttpServletRequest request, HttpServletResponse response, String... acceptedParams) {
        long current = head(getRevisionsNewestFirst());
        if (current != revision.revision) {
            ResponseUtil.redirectToAnotherRevision(current, request, response);
            return None();
        }
        return Some(checkUrlAndResolveFormat(request, response, acceptedParams));
    }
}
