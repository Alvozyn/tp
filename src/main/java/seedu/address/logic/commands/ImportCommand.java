package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.storage.Importer;
import seedu.address.storage.JsonImporter;
import seedu.address.storage.exceptions.EmptyAddressBookException;

public class ImportCommand extends Command {
    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Import the person data contained in an "
            + "export file. If the person already exists, the personal information and events are updated. "
            + "If not, a new person is added.\n"
            + "File must be stored in same directory and must be named \"export.json\" to be read.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_IMPORT_PERSON_SUCCESS = "Import Person: %1$s";

    private final Path importPath = Path.of("data", "export.json");

    private Person personToAdd;

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws seedu.address.logic.commands.exceptions.CommandException If an error occurs during command execution.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Importer importer = new JsonImporter(importPath);
        Optional<ReadOnlyAddressBook> readOnlyAddressBookOptional;

        try {
            readOnlyAddressBookOptional = importer.readData();
        } catch (DataConversionException e) {
            throw new CommandException(Messages.MESSAGE_INVALID_DATA);
        } catch (EmptyAddressBookException e) {
            throw new CommandException(Messages.MESSAGE_NO_IMPORT);
        }

        if (readOnlyAddressBookOptional.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_NO_IMPORT);
        }
        ReadOnlyAddressBook importReadOnlyAddressBook = readOnlyAddressBookOptional.get();
        List<Person> importPersonList = importReadOnlyAddressBook.getPersonList();

        if (importPersonList.size() != 1) {
            throw new CommandException(Messages.MESSAGE_IMPORT_SIZE_WRONG);
        }

        Person importedPerson = importPersonList.get(0);

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        List<Person> allPersonList = model.getFilteredPersonList();

        for (Person listPerson : allPersonList) {
            if (listPerson.isSamePerson(importedPerson)) {
                personToAdd = createImportPerson(importedPerson, listPerson);
                model.setPerson(listPerson, personToAdd);
                return new CommandResult(String.format(MESSAGE_IMPORT_PERSON_SUCCESS, personToAdd));
            }
        }
        personToAdd = importedPerson;
        model.addPerson(personToAdd);
        return new CommandResult(String.format(MESSAGE_IMPORT_PERSON_SUCCESS, personToAdd));
    }

    private static Person createImportPerson(Person importData, Person existingData) {
        return new Person(importData.getName(),
                importData.getPhone(),
                importData.getEmail(),
                importData.getAddress(),
                existingData.getTags(), // Do not replace tags
                existingData.getGroups(), // Do not replace groups
                importData.getIsolatedEventList().getSet(),
                importData.getRecurringEventList().getSet());
    }
}
