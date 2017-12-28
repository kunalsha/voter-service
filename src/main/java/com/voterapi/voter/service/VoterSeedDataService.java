package com.voterapi.voter.service;

import com.voterapi.voter.domain.CandidateVoterView;
import com.voterapi.voter.domain.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class VoterSeedDataService {

    private CandidateService candidateList;
    private List<Vote> votes = new ArrayList<>();

    @Autowired
    public VoterSeedDataService(CandidateService candidateList) {
        this.candidateList = candidateList;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    // accepts map of candidates and total votes
    public void votesFromMap(Map<String, String> candidates, String election) {
        votes.clear(); // clear previous seed data from list
        for (Map.Entry<String, String> entry : candidates.entrySet()) {
            String key = entry.getKey();
            int value = Integer.parseInt(entry.getValue());

            for (int i = 0; i < value; i++) {
                String candidate = String.valueOf(key);
                votes.add(new Vote(candidate, election));
            }
        }
    }

    // generates random number of total votes for each candidate using Queue to DB
    public void setRandomVotesDb(String election) {
        Map candidates = new HashMap();
        List<CandidateVoterView> list = candidateList.getCandidatesQueueDb(election);
        for (CandidateVoterView aList : list) {
            candidates.put(aList.getFullName(), getRandomIntAsString(3, 25));
        }
        votesFromMap(candidates, election);
    }

    // returns random number as string
    private String getRandomIntAsString(int min, int max) {
        int randomVoteCount = ThreadLocalRandom.current().nextInt(min, max + 1);
        return Integer.toString(randomVoteCount);
    }
}
