package de.uniks.stp24.model;

import de.uniks.stp24.dto.TechnologyDto;

import java.util.ArrayList;
import java.util.List;

public class TechnologieNode {

    public List<TechnologieNode> children = new ArrayList<>();
    public List<TechnologieNode> parents = new ArrayList<>();

    private final TechnologyDto technologyDto;
    private final String name;

    private int x , y;


    public TechnologieNode(TechnologyDto technologyDto) {
        this.technologyDto = technologyDto;
        this.name = findName(technologyDto.id());
    }

    public TechnologyDto getTechnology() {
        return technologyDto;
    }

    public List<TechnologieNode> getChildren() {
        return children;
    }

    public List<TechnologieNode> getParents() {
        return parents;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }
    public String findName(String id) {
        return switch (id) {
            case "society" -> "Social Advancement";
            case "demographic" -> "Population Dynamics";
            case "computing" -> "Digital Revolution";
            case "engineering" -> "Industrial Ingenuity";
            case "construction" -> "Construction Mastery";
            case "production" -> "Production Optimization";
            case "cheap_claims_1" -> "Territorial Expansion";
            case "cheap_claims_2" -> "Efficient Colonization";
            case "cheap_claims_3" -> "Galactic Dominion";
            case "efficient_systems_1" -> "Energy Grid Optimization";
            case "efficient_systems_2" -> "Propulsion Efficiency";
            case "efficient_systems_3" -> "Resource Management";
            case "efficient_systems_4" -> "Alloy Streamlining";
            case "cheap_buildings_1" -> "Efficient Structures";
            case "cheap_buildings_2" -> "Cost-Effective Facilities";
            case "cheap_buildings_3" -> "Scrapyard Optimization";
            case "efficient_buildings_1" -> "Energy Conservation";
            case "efficient_buildings_2" -> "Power Grid Optimization";
            case "efficient_buildings_3" -> "Resource Efficiency";
            case "improved_production_1" -> "Productivity Boost";
            case "improved_production_2" -> "Efficient Manufacturing";
            case "improved_production_3" -> "Advanced Production";
            case "improved_production_4" -> "Masterwork Facilities";
            case "efficient_resources_1" -> "Resource Optimization";
            case "efficient_resources_2" -> "Material Efficiency";
            case "district_specialization" -> "District Specialization";
            case "district_production_increase" -> "Productive Districts";
            case "ancient_district_production_increase" -> "Ancient Productivity";
            case "district_cost_reduction" -> "Cost-Effective Districts";
            case "ancient_district_cost_reduction" -> "Ancient Cost Reduction";
            case "district_upkeep_reduction" -> "Low-Maintenance Districts";
            case "ancient_district_upkeep_reduction" -> "Ancient Upkeep Reduction";
            case "ancient_mastery" -> "Ancient Mastery";
            case "ancient_military_activation" -> "Military Augmentation";
            case "ancient_military_1" -> "Martial Superiority";
            case "ancient_military_2" -> "Warfront Dominance";
            case "ancient_military_3" -> "Galactic Supremacy";
            case "ancient_industry_activation" -> "Industrial Awakening";
            case "ancient_industry_1" -> "Productive Legacy";
            case "ancient_industry_2" -> "Efficient Automation";
            case "ancient_industry_3" -> "Masterwork Production";
            case "ancient_tech_activation" -> "Technological Rebirth";
            case "ancient_tech_1" -> "Quantum Breakthrough";
            case "ancient_tech_2" -> "Scientific Renaissance";
            case "ancient_tech_3" -> "Singularity Mastery";
            case "mining_foundation_1" -> "Efficient Mining";
            case "mining_foundation_2" -> "Optimized Extraction";
            case "mining_foundation_3" -> "Deep Vein Mastery";
            case "efficient_ancient_foundry_1" -> "Foundry Optimization";
            case "efficient_ancient_foundry_2" -> "Alloy Efficiency";
            case "efficient_ancient_foundry_3" -> "Masterwork Foundry";
            case "efficient_ancient_refinery_1" -> "Refinery Optimization";
            case "efficient_ancient_refinery_2" -> "Fuel Efficiency";
            case "efficient_ancient_refinery_3" -> "Eternal Refinery";
            case "efficient_city_1" -> "Urban Efficiency";
            case "efficient_city_2" -> "Metropolis Optimization";
            case "efficient_city_3" -> "Civic Mastery";
            case "efficient_industry_1" -> "Industrial Efficiency";
            case "efficient_industry_2" -> "Streamlined Manufacturing";
            case "efficient_industry_3" -> "Optimized Production";
            case "improved_industry_1" -> "Industrial Boost";
            case "improved_industry_2" -> "Efficient Factories";
            case "improved_industry_3" -> "Masterwork Industry";
            case "pop_food_consumption_1" -> "Dietary Optimization";
            case "pop_food_consumption_2" -> "Nutritional Efficiency";
            case "pop_food_consumption_3" -> "Sustainable Nourishment";
            case "pop_growth_colonized_1" -> "Colonized Growth";
            case "pop_growth_colonized_2" -> "Expansive Development";
            case "pop_growth_colonized_3" -> "Galactic Propagation";
            case "pop_growth_upgraded_1" -> "Upgraded Growth";
            case "pop_growth_upgraded_2" -> "Advanced Development";
            case "pop_growth_upgraded_3" -> "Thriving Civilization";
            case "unemployed_pop_cost_1" -> "Efficient Workforce";
            case "unemployed_pop_cost_2" -> "Streamlined Labor";
            case "unemployed_pop_cost_3" -> "Optimized Productivity";
            case "energy_production_1" -> "Power Amplification";
            case "energy_production_2" -> "Energy Maximization";
            case "energy_production_3" -> "Unlimited Power";
            case "mineral_production_1" -> "Mining Productivity";
            case "mineral_production_2" -> "Extraction Mastery";
            case "mineral_production_3" -> "Endless Minerals";
            case "food_production_1" -> "Agricultural Boost";
            case "food_production_2" -> "Bountiful Harvest";
            case "food_production_3" -> "Culinary Abundance";
            case "research_production_1" -> "Scientific Focus";
            case "research_production_2" -> "Innovative Mindset";
            case "research_production_3" -> "Mind Over Matter";
            case "alloy_production_1" -> "Refined Metallurgy";
            case "alloy_production_2" -> "Masterwork Alloys";
            case "alloy_production_3" -> "Unlimited Alloys";
            case "fuel_production_1" -> "Fuel Efficiency";
            case "fuel_production_2" -> "Refined Propellants";
            case "fuel_production_3" -> "Infinite Propulsion";
            case "energy_district_production_1" -> "Solar Mastery";
            case "energy_district_production_2" -> "Power Grid Nexus";
            case "energy_district_production_3" -> "Supernova Reactor";
            case "mining_district_production_1" -> "Rich Veins";
            case "mining_district_production_2" -> "Bountiful Excavation";
            case "mining_district_production_3" -> "Planetary Strip Mining";
            case "agriculture_district_production_1" -> "Fertile Cultivation";
            case "agriculture_district_production_2" -> "Agricultural Nexus";
            case "agriculture_district_production_3" -> "Culinary Singularity";
            case "research_site_production_1" -> "Knowledge Cultivation";
            case "research_site_production_2" -> "Quantum Theories";
            case "research_site_production_3" -> "Universal Understanding";
            case "ancient_foundry_production_1" -> "Forging Legacy";
            case "ancient_foundry_production_2" -> "Forged Efficiency";
            case "ancient_foundry_production_3" -> "Masterwork Forge";
            case "ancient_refinery_production_1" -> "Fuel Synthesis";
            case "ancient_refinery_production_2" -> "Refined Output";
            case "ancient_refinery_production_3" -> "Eternal Reserves";
            case "city_production_1" -> "Economic Stimulation";
            case "city_production_2" -> "Urban Prosperity";
            case "city_production_3" -> "Galactic Marketplace";
            case "market_fee_reduction_1" -> "Fair Trade";
            case "market_fee_reduction_2" -> "Free Market";
            case "market_fee_reduction_3" -> "Economic Singularity";
            case "effective_energy_1" -> "Solar Cultivation";
            case "effective_energy_2" -> "Clean Energy";
            case "effective_energy_3" -> "Perpetual Power";
            case "efficient_energy_1" -> "Energy Streamlining";
            case "efficient_energy_2" -> "Power Grid Efficiency";
            case "efficient_energy_3" -> "Optimized Energy";
            case "efficient_mining_1" -> "Mining Optimization";
            case "efficient_mining_2" -> "Resource Maximization";
            case "efficient_mining_3" -> "Deep Excavation";
            case "agriculture_cost_reduction_1" -> "Fertile Soils";
            case "agriculture_cost_reduction_2" -> "Rich Farmlands";
            case "agriculture_cost_reduction_3" -> "Abundant Fields";
            case "efficient_agriculture_1" -> "Water Conservation";
            case "efficient_agriculture_2" -> "Smart Farming";
            case "efficient_agriculture_3" -> "Agricultural Singularity";
            case "effective_lab_building_1" -> "Lab Infrastructure";
            case "effective_lab_building_2" -> "Knowledge Centers";
            case "effective_lab_building_3" -> "Wisdom Nexus";
            case "efficient_research_1" -> "Scientific Method";
            case "efficient_research_2" -> "Innovative Approach";
            case "efficient_research_3" -> "Brilliant Discoveries";
            case "ancient_foundry_structure_1" -> "Ancient Forges";
            case "ancient_foundry_structure_2" -> "Timeless Production";
            case "ancient_foundry_structure_3" -> "Molten Mastery";
            case "ancient_refinery_structure_1" -> "Ancient Refineries";
            case "ancient_refinery_structure_2" -> "Timeless Energy";
            case "ancient_refinery_structure_3" -> "Eternal Propulsion";
            case "city_structure_1" -> "Urban Planning";
            case "city_structure_2" -> "Metropolitan Development";
            case "city_structure_3" -> "Arcology Blueprint";
            case "industry_structure_1" -> "Factory Foundations";
            case "industry_structure_2" -> "Industrial Efficiency";
            case "industry_structure_3" -> "Optimized Facilities";
            case "more_colonists_1" -> "Wasteland Recruits";
            case "more_colonists_2" -> "Wandering Civilization";
            case "more_colonists_3" -> "Wave of Colonists";
            case "faster_explored_system_upgrade_1"  -> "Wasteland Scout Tactics";
            case "faster_explored_system_upgrade_2"  -> "Pathfinder Advancements";
            case "faster_explored_system_upgrade_3"  -> "Exploration Pioneers";
            case "faster_research_1"  -> "Scrap Science";
            case "faster_research_2"  -> "Vault-Tec Innovations";
            case "faster_research_3"  -> "Pre-War Tech Revival";
            case "faster_upgraded_system_upgrade_1"  -> "Jury-Rigging Mastery";
            case "faster_upgraded_system_upgrade_2"  -> "Enhanced Reclamation";
            case "faster_upgraded_system_upgrade_3"  -> "Advanced Retrofit Techniques";
            case "faster_district_construction_1"  -> "Settlement Expedite";
            case "faster_district_construction_2"  -> "Rapid Expansion Methods";
            case "faster_district_construction_3"  -> "High-Speed Habitat Construction";
            case "faster_colonized_system_upgrade_1"  -> "Colonist Boost";
            case "faster_colonized_system_upgrade_2"  -> "Enhanced Pioneer Tools";
            case "faster_colonized_system_upgrade_3"  -> "Settler Surge";
            case "faster_building_construction_1"  -> "Pre-Fab Construction";
            case "faster_building_construction_2"  -> "Expedited Architecture";
            case "faster_building_construction_3"  -> "Rapid Erection Protocols";
            case "faster_developed_system_upgrade_1"  -> "Post-War Efficiency";
            case "faster_developed_system_upgrade_2"  -> "Optimized Development";
            case "faster_developed_system_upgrade_3"  -> "Master Builder Techniques";
            case "biology_specialization" -> "Bio-Enhanced Warfare";
            case "economy_specialization" -> "Industrial Supremacy";
            case "building_specialization" -> "Atomic Infrastructure";
            case "system_specialization" -> "Fraction Specialization";
            case "ship_construction" -> "Tank Operations";
            case "small_ship_construction" -> "Special Tank Operations";
            case "fast_small_ship_construction_1" -> "Special Tank Mission 1";
            case "fast_small_ship_construction_2" -> "Special Tank Mission 2";
            case "fast_small_ship_construction_3" -> "Special Tank Mission 3";
            case "medium_ship_construction" -> "Combat Tank Operations";
            case "fast_medium_ship_construction_1" -> "Combat Tank Mission 1";
            case "fast_medium_ship_construction_2" -> "Combat Tank Mission 2";
            case "fast_medium_ship_construction_3" -> "Combat Tank Mission 3";
            case "large_ship_construction" -> "Heavy Tank Operations";
            case "fast_large_ship_construction_1" -> "Heavy Tank Mission 1";
            case "fast_large_ship_construction_2" -> "Heavy Tank Mission 2";
            case "fast_large_ship_construction_3" -> "Heavy Tank Mission 3";
            case "fast_ship_construction_1" -> "Rapid Tank Mission 1";
            case "fast_ship_construction_2" -> "Rapid Tank Mission 2";
            case "fast_ship_construction_3" -> "Rapid Tank Mission 3";
            case "armor_plating_1" -> "Scouting Tank Armor";
            case "armor_plating_2" -> "Assault Tank Armor";
            case "armor_plating_3" -> "Elite Tank Armor";
            case "armor_plating_4" -> "Heavy Tank Armor";
            case "ship_speed_1" -> "Scouting Tank Speed";
            case "ship_speed_2" -> "Assault Tank Speed";
            case "ship_speed_3" -> "Elite Tank Speed";
            case "ship_speed_4" -> "Heavy Tank Speed";
            case "cheap_ships_1" -> "Cheap Scouting Tanks";
            case "cheap_ships_2" -> "Cheap Assault Tanks";
            case "cheap_ships_3" -> "Cheap Elite Tanks";
            case "cheap_ships_4" -> "Cheap Heavy Tanks";
            case "efficient_ships_1" -> "Efficient Scouting Tanks";
            case "efficient_ships_2" -> "Efficient Assault Tanks";
            case "efficient_ships_3" -> "Efficient Elite Tanks";
            case "efficient_ships_4" -> "Efficient Heavy Tanks";
            case "small_fighters_1" -> "Assault Tanks 1";
            case "small_fighters_2" -> "Assault Tanks 2";
            case "medium_fighters_1" -> "Elite Tanks 1";
            case "medium_fighters_2" -> "Elite Tanks 2";
            case "large_fighters_1" -> "Heavy Tanks 1";
            case "large_fighters_2" -> "Heavy Tanks 2";
            case "small_ship_defense_1" -> "Assault Tank Defense 1";
            case "small_ship_defense_2" -> "Assault Tank Defense 2";
            case "medium_ship_defense_1" -> "Elite Tank Defense 1";
            case "medium_ship_defense_2" -> "Elite Tank Defense 2";
            case "large_ship_defense_1" -> "Heavy Tank Defense 1";
            case "large_ship_defense_2" -> "Heavy Tank Defense 2";
            default -> id;
        };
    }
}
