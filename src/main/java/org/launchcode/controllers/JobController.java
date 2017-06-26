package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.data.JobFieldData;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {
        //Sort of like a table of contents
        // TODO #1 - get the Job with the given ID and pass it into the view. DID IT
        // Remember to import class to use a class from another file.
        Job singleJob = jobData.findById(id);

        model.addAttribute("id",id);
        model.addAttribute("job", singleJob);
        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors) {
        // @Valid adds validation on the form submission.
        // TODO #6 - Validate the JobForm model, and if valid, create a
        // new Job and add it to the jobData data store. Then
        // redirect to the job detail view for the new Job.

        if (errors.hasErrors()) {
            model.addAttribute(jobForm);
            return "new-job";
        }
        String name = jobForm.getName();
        int employerId = jobForm.getEmployerId(); // for instance the number 8 or 3
        JobFieldData<Employer> employersList = jobData.getEmployers();
        Employer employerObject = employersList.findById(employerId);

        int locationId = jobForm.getLocationId(); // for instance the number 11
        JobFieldData<Location> locationsList = jobData.getLocations();
        Location locationObject = locationsList.findById(locationId);

        int positionTypeId = jobForm.getPositionTypeId();
        JobFieldData<PositionType> positionTypesList = jobData.getPositionTypes();
        PositionType positionTypeObject = positionTypesList.findById(positionTypeId);

        int coreCompetencyId = jobForm.getCoreCompetencyId(); // for instance the number 18
        JobFieldData<CoreCompetency> coreCompetenciesList = jobData.getCoreCompetencies();
        CoreCompetency coreCompetencyObject = coreCompetenciesList.findById(coreCompetencyId);

        Job newJobListing = new Job(name, employerObject, locationObject, positionTypeObject, coreCompetencyObject);

        jobData.add(newJobListing);
        model.addAttribute("job", newJobListing);
        return "job-detail";
    }
}

