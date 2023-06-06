package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.configs.security.jwt.TokenProvider;
import com.berryinkstamp.berrybackendservice.dtos.request.ReasonRequest;
import com.berryinkstamp.berrybackendservice.dtos.request.UpdateProfileRequest;
import com.berryinkstamp.berrybackendservice.enums.ProfileStatus;
import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import com.berryinkstamp.berrybackendservice.exceptions.ForbiddenException;
import com.berryinkstamp.berrybackendservice.exceptions.NotFoundException;
import com.berryinkstamp.berrybackendservice.models.Admin;
import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.models.Profile;
import com.berryinkstamp.berrybackendservice.models.User;
import com.berryinkstamp.berrybackendservice.repositories.ProfileRepository;
import com.berryinkstamp.berrybackendservice.services.ProfileService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final TokenProvider tokenProvider;
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<Profile> getAllProfiles(ProfileType profileType, Pageable pageable) {
        return profileRepository.findByProfileType(profileType, pageable);
    }

    @Override
    public Profile getUserProfile(Long profileId) {
        Optional<Profile> profile = profileRepository.findById(profileId);
        if (profile.isEmpty()) {
            throw new NotFoundException("user profile not found");
        }
        return profile.get();
    }

    @Override
    public Profile updateProfile(UpdateProfileRequest request, ProfileType profileType) {
        Profile profile = tokenProvider.getCurrentUser().getProfile(profileType);
        if (profile == null) {
            throw new NotFoundException("profile not found");
        }

        profile.setName(request.getName());
        profile.setBio(request.getBio());
        profile.setCategories(String.join(",", request.getCategories()));
        profile.setProfilePic(request.getProfilePic());
        profile.setCoverPic(request.getCoverPic());
        profile = profileRepository.save(profile);
        return profile;
    }

    @Override
    public Page<Profile> adminGetAllProfiles(ProfileType profile, String field, Pageable pageable) {
        Admin admin = tokenProvider.getCurrentAdmin();
        if (!admin.isSuperAdmin() && !admin.getPermission().isCanManagerUsers()) {
            throw new ForbiddenException("you do not have enough permission to manage this resource");
        }

        if (profile == null && field == null) {
            return profileRepository.findAll(pageable);
        }
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Profile> cq = qb.createQuery(Profile.class);

        Root<Profile> root = cq.from(Profile.class);

        List<Predicate> predicates = getPredicates(profile, field, qb, root, cq);

        cq.select(root).where(predicates.toArray(new Predicate[]{}));

        List<Profile> res = entityManager.createQuery(cq).getResultList();


        return new PageImpl<>(res, pageable, res.size());
    }

    @Override
    public Profile suspendProfile(Long profileId, ReasonRequest request) {
        Admin admin = tokenProvider.getCurrentAdmin();
        if (!admin.isSuperAdmin() && !admin.getPermission().isCanManagerUsers()) {
            throw new ForbiddenException("you do not have enough permission to manage this resource");
        }
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);
        if (optionalProfile.isEmpty()) {
            throw new NotFoundException("user profile not found");
        }

        Profile profile = optionalProfile.get();
        profile.setStatus(ProfileStatus.PROBATION);
        profile.setProbationDate(LocalDateTime.now());
        profile.setReasonForProbation(request.getReason());

        profile =  profileRepository.save(profile);
        //todo send email push to notification
        return profile;
    }

    @Override
    public Profile terminateProfile(Long profileId, ReasonRequest request) {
        Admin admin = tokenProvider.getCurrentAdmin();
        if (!admin.isSuperAdmin() && !admin.getPermission().isCanManagerUsers()) {
            throw new ForbiddenException("you do not have enough permission to manage this resource");
        }
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);
        if (optionalProfile.isEmpty()) {
            throw new NotFoundException("user profile not found");
        }

        Profile profile = optionalProfile.get();
        profile.setStatus(ProfileStatus.TERMINATED);
        profile.setTerminationDate(LocalDateTime.now());
        profile.setReasonForTermination(request.getReason());

        profile =  profileRepository.save(profile);
        //todo send email push to notification
        return profile;
    }

    @Override
    public Profile activateProfile(Long profileId) {
        Admin admin = tokenProvider.getCurrentAdmin();
        if (!admin.isSuperAdmin() && !admin.getPermission().isCanManagerUsers()) {
            throw new ForbiddenException("you do not have enough permission to manage this resource");
        }
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);
        if (optionalProfile.isEmpty()) {
            throw new NotFoundException("user profile not found");
        }

        Profile profile = optionalProfile.get();
        profile.setStatus(ProfileStatus.ACTIVE);
        profile.setTerminationDate(null);
        profile.setProbationDate(null);
        profile.setReasonForProbation(null);
        profile.setReasonForTermination(null);

        return profileRepository.save(profile);
    }

    private List<Predicate> getPredicates(ProfileType profileType, String field, CriteriaBuilder qb, Root<Profile> root, CriteriaQuery<Profile> cq) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(qb.isFalse(root.get("deleted")));

        predicates.add(qb.isTrue(root.get("approved")));

        if (Objects.nonNull(profileType)) {
            predicates.add(qb.equal(root.get("profileType"), profileType));
        }

        if (Objects.nonNull(field)) {
            predicates.add(qb.like(root.get("name"), "%" + field + "%"));
        }

        if (Objects.nonNull(field)) {
            predicates.add(qb.like(root.get("profileType"), "%" + field + "%"));
        }

        Subquery<Long> subquery = cq.subquery(Long.class);
        Root<Profile> subqueryStudent = subquery.from(Profile.class);
        Join<User, Profile> subqueryCourse = subqueryStudent.join("user");

        subquery.select(subqueryStudent.get("id")).where(qb.like(subqueryCourse.get("email"), "%" + field + "%"));

        predicates.add(qb.in(root.get("id")).value(subquery));



        Subquery<Long> subquery2 = cq.subquery(Long.class);
        Root<Profile> subqueryStudent2 = subquery2.from(Profile.class);
        Join<User, Profile> subqueryCourse2 = subqueryStudent2.join("user");

        subquery2.select(subqueryStudent2.get("id")).where(qb.like(subqueryCourse2.get("phoneNumber"), "%" + field + "%"));

        predicates.add(qb.in(root.get("id")).value(subquery2));


        return predicates;
    }

}
