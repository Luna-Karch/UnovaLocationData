package program.DataTypes.Classes;

import java.util.ArrayList;
import program.DataTypes.Enums.FilterType;

public class Collection {
    private ArrayList<Entry> collection;
    public String locationName;
    public Collection(String locationName) {
        this.collection = Entry.readLocation("data/encounterData.csv", locationName);
        this.locationName = locationName;
    }

    public Collection(ArrayList<Entry> collection) {
        this.collection = collection; // Alternate constructor for chaining
        this.locationName = "N/A";
    }

    public Collection() {
        this.collection = new ArrayList<>(); // Create empty collection
        this.locationName = "N/A"; // Creating an empty collection
    }

    public ArrayList<Entry> getAllEntries() {
        return this.collection;
    }

    public String getLocationName() {
        return this.locationName;
    }

    public void printAllEntries() {
        for (int i = 0; i < collection.size(); i++) {
            System.out.println(collection.get(i));
        }
    }
    private Collection filterEncounterType(String filterString) {
        ArrayList<Entry> result = new ArrayList<Entry>();

        for (int i = 0; i < this.collection.size(); i++) {
            Entry currentEntry = this.collection.get(i);

            if (currentEntry.getEncounterType().toLowerCase().equals(filterString)) {
                // If the encounter types match add the entry to the ArrayList
                result.add(currentEntry);
            }
        }

        return new Collection(result);
    };

    private Collection filterFloor(String filterString) {
        ArrayList<Entry> result = new ArrayList<Entry>();

        for (int i = 0; i < this.collection.size(); i++) {
            Entry currentEntry = this.collection.get(i);

            if (currentEntry.getFloor().toLowerCase().equals(filterString)) {
                // If floor matches
                result.add(currentEntry);
            }
        }

        return new Collection(result);
    };

    private Collection filterGameVersion(String filterString) {
        ArrayList<Entry> result = new ArrayList<Entry>();

        for (int i = 0; i < this.collection.size(); i++) {
            Entry currentEntry = this.collection.get(i);
            String[] gameVersions = currentEntry.getGameVersionsArray();

            for (int j = 0; j < gameVersions.length; j++) {
                String gameVersionAsLowerCase = gameVersions[j].toLowerCase();
                if (gameVersionAsLowerCase.equals(filterString)) {
                    result.add(currentEntry); // Add the Entry
                    break; // Exit inner loop so multiple of the same Entry aren't added
                }
            }
        }

        return new Collection(result);
    };

    private Collection filterLevel(String filterString) {
        ArrayList<Entry> result = new ArrayList<>();

        if (!filterString.contains("-")) {
            try { // no range found
                int value = Integer.parseInt(filterString);
                for (Entry entry: this.collection) {
                    int[] possibleLevels = entry.getPossibleLevels();
                    for (int possibleLevel: possibleLevels) {
                        if (possibleLevel == value) {
                            result.add(entry);
                            break; // Leave loop early
                        } // if a match for the value is found
                    }
                }
            } catch (NumberFormatException e) {
                return this; // if anything breaks, act like nothing happened
            }
        } else {
            String[] values = filterString.split("-");
            try {
                int minimum = Integer.parseInt(values[0]);
                int maximum = Integer.parseInt(values[1]);

                boolean hasBroken = false;
                for (Entry entry: this.collection) {
                    int[] possibleLevels = entry.getPossibleLevels();
                    for (int possibleLevel: possibleLevels) {
                        for (int i = minimum; i <= maximum; i++) {
                            if (i == possibleLevel) {
                                result.add(entry);
                                hasBroken = true;
                                break; // should exit to the possible levels
                                // loop
                            }
                        }

                        if (hasBroken) {
                            hasBroken = false;
                            break; // should exit to the entry loop
                        }
                    }
                }
            } catch (NumberFormatException e) {
                return this; // act like nothing happened if it breaks
            }
        }

        return new Collection(result);
    };

    private Collection filterModifier(String filterString) {
        ArrayList<Entry> result = new ArrayList<Entry>();
        
        for (int i = 0; i < this.collection.size(); i++) {
            Entry currentEntry = this.collection.get(i);
            
            if (currentEntry.getModifier().toLowerCase().equals(filterString)) {
                result.add(currentEntry);
            }

        }

        return new Collection(result);
    };

    private Collection filterPokemonName(String filterString) {
        ArrayList<Entry> result = new ArrayList<Entry>();
        for (int i = 0; i < this.collection.size(); i++) {
            Entry currentEntry = this.collection.get(i);

            if (currentEntry.getPokemonName().toLowerCase().equals(filterString)) {
                result.add(currentEntry);
            }
        }

        return new Collection(result);
    };

    private Collection filterRarity(String filterString) {
        ArrayList<Entry> result = new ArrayList<Entry>();
        for (int i = 0; i < this.collection.size(); i++) {
            Entry currentEntry = this.collection.get(i);

            if (currentEntry.getRarity().toLowerCase().equals(filterString)) {
                result.add(currentEntry);
            }
        }

        return new Collection(result);
    };

    private Collection filterSeason(String filterString) {
        ArrayList<Entry> result = new ArrayList<Entry>();
        for (int i = 0; i < this.collection.size(); i++) {
            Entry currentEntry = this.collection.get(i);
            String[] seasons = currentEntry.getSeasonsArray();

            for (int j = 0; j < seasons.length; j++) {
                if (seasons[j].toLowerCase().equals(filterString)) {
                    result.add(currentEntry);
                    break;
                }
            }
        }

        return new Collection(result);

    };

    private Collection filterLocation(String filterString) {
        ArrayList<Entry> result = new ArrayList<Entry>();
        for (int i = 0; i < this.collection.size(); i++) {
            Entry currentEntry = this.collection.get(i);

            if (currentEntry.getLocation().toLowerCase().equals(filterString)) {
                result.add(currentEntry);
            }
        }

        return new Collection(result);
    };

    public Collection filter(FilterType method, String filterString) {
        filterString = filterString.toLowerCase();

        switch (method) {
            case ENCOUNTER_TYPE:
                return this.filterEncounterType(filterString);
            case FLOOR:
                return this.filterFloor(filterString);
            case GAME_VERSION:
                return this.filterGameVersion(filterString);
            case LEVEL:
                return this.filterLevel(filterString);
            case MODIFIER:
                return this.filterModifier(filterString);
            case POKEMON_NAME:
                return this.filterPokemonName(filterString);
            case RARITY:
                return this.filterRarity(filterString);
            case SEASON:
                return this.filterSeason(filterString);
            case LOCATION:
                return this.filterLocation(filterString);
            default:
                return this; // If the method is somehow unknown, give back the original collection
        }
    }

    public Collection combine(Object object) {
        if (!(object instanceof Collection)) {
            return null;
        }

        ArrayList<Entry> result = this.collection;
        Collection otherCollection = (Collection)object;
        ArrayList<Entry> otherEntries = otherCollection.getAllEntries();

        for (int i = 0; i < otherEntries.size(); i++) {
            result.add(otherEntries.get(i));
        }

        return new Collection(result);
    }
}
