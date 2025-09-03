package de.uniks.stp24.service;

import de.uniks.stp24.dto.TechnologyDto;
import de.uniks.stp24.model.TechnologieNode;
import de.uniks.stp24.rest.EmpireApiService;
import de.uniks.stp24.rest.PresetsApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TechnologiesServices {

    public final Map<String, TechnologyDto> technologiesCache = new HashMap<>();
    final List<TechnologieNode> technologieTree = new ArrayList<>();
    private final List<String> unlockedTechnologies = new ArrayList<>();
    final List<TechnologyDto> allTechnologies = new ArrayList<>();
    final Map<String, TechnologieNode> technologieNodeMap = new ConcurrentHashMap<>();

    @Inject
    GameService gameService;

    @Inject
    PresetsApiService presetsApiService;
    @Inject
    EmpireApiService empireApiService;

    @Inject
    public TechnologiesServices() {
    }


    Observable<List<String>> loadUnlockedTechnologies() {
        if(gameService.getGame() == null || gameService.getOwnUser() == null || gameService.getOwnUser().getEmpire() == null) {
            return Observable.just(Collections.emptyList());
        }
        return empireApiService.getById(gameService.getGame().getId(), gameService.getOwnUser().getEmpire().get_id())
                .map(res -> {
                    synchronized (unlockedTechnologies) {
                        unlockedTechnologies.clear();
                        unlockedTechnologies.addAll(res.technologies());
                        return unlockedTechnologies;
                    }
                });
    }
    private List<String> getUnlockedTechnologies() {
        if(gameService.getGame() == null || gameService.getOwnUser() == null || gameService.getOwnUser().getEmpire() == null) {
            return Collections.emptyList();
        }
        List<String> unlockedTechnologies = new ArrayList<>();
        gameService.getOwnUser().getEmpire().getTechnologies().forEach(tech -> unlockedTechnologies.add(tech.get_id()));
        return unlockedTechnologies;
    }

    public Boolean isUnlocked(TechnologieNode node) {
        return getUnlockedTechnologies().contains(node.getTechnology().id());
    }

    public boolean areAllRequirementsUnlocked(TechnologieNode node) {
        if (node.getTechnology().requires() == null) {
            return true;
        }
        for (String requiredTech : node.getTechnology().requires()) {
            if (!getUnlockedTechnologies().contains(requiredTech)) {
                return false;
            }
        }
        return true;
    }

    Observable<Collection<TechnologyDto>> loadTechnologies() {
        technologieTree.clear();
        allTechnologies.clear();
        return presetsApiService.getTechnologies()
                .map(res -> {
                    synchronized (technologiesCache) {
                        List<TechnologyDto> technologyDtoList = res.body();
                        if (technologyDtoList != null) {
                            technologiesCache.clear();
                            for (TechnologyDto technologyDto : technologyDtoList) {
                                technologiesCache.put(technologyDto.id(), technologyDto);
                                allTechnologies.add(technologyDto);
                            }
                        }
                    }
                    return technologiesCache.values();
                });
    }

    public Observable<List<TechnologieNode>> loadRoots() {
        return Observable.fromCallable(() -> {
            List<TechnologieNode> roots = new ArrayList<>();
            synchronized (technologiesCache) {
                Iterator<Map.Entry<String, TechnologyDto>> iterator = technologiesCache.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, TechnologyDto> entry = iterator.next();
                    TechnologyDto tech = entry.getValue();
                    if (tech.requires() == null || tech.requires().isEmpty()) {
                        TechnologieNode root = new TechnologieNode(tech);
                        roots.add(root);
                        technologieTree.add(root);
                        iterator.remove();
                    }
                }
            }
            return roots;
        });
    }

    private Observable<List<TechnologieNode>> findChildren(TechnologieNode parent) {
        return Observable.fromCallable(() -> {
            List<TechnologieNode> children = new ArrayList<>();
            synchronized (technologiesCache) {
                Iterator<Map.Entry<String, TechnologyDto>> iterator = technologiesCache.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, TechnologyDto> entry = iterator.next();
                    TechnologyDto tech = entry.getValue();
                    if (tech.requires() != null && tech.requires().contains(parent.getTechnology().id())) {
                        TechnologieNode child = new TechnologieNode(tech);
                        children.add(child);
                        child.getParents().add(parent);
                        technologieNodeMap.put(tech.id(), child);
                        iterator.remove();
                    }
                }
            }
            return children;
        });
    }

    private Observable<TechnologieNode> loadTreeNode(TechnologieNode node) {
        return findChildren(node)
                .flatMap(children -> Observable.fromIterable(children)
                        .flatMap(childNode ->{
                            if(isUnlocked(childNode)) {
                                return loadTreeNode(childNode);
                            } else {
                                return Observable.just(childNode);
                            }
                        }
                        )
                        .toList()
                        .toObservable()
                        .doOnNext(node.getChildren()::addAll)
                ).map(children -> node);
    }

    public Observable<List<TechnologieNode>> loadTree() {
        technologieTree.clear();
        return loadTechnologies()
                .flatMap(techs -> loadUnlockedTechnologies()
                        .flatMap(unlockedTechs -> loadRoots()
                                .flatMap(roots -> Observable.fromIterable(roots)
                                        .flatMap(root -> loadTreeNode(root).map(tree -> {
                                            Set<TechnologieNode> set = loadSet(tree);
                                            for (TechnologieNode child : set) {
                                                TechnologyDto dto = child.getTechnology();
                                                if (dto.requires() != null) {
                                                    for (String requiredTech : dto.requires()) {
                                                        TechnologieNode requiredNode = technologieNodeMap.get(requiredTech);
                                                        if (requiredNode != null && !requiredNode.getChildren().contains(child) && !child.getParents().contains(requiredNode)) {
                                                            requiredNode.getChildren().add(child);
                                                            child.getParents().add(requiredNode);
                                                        }
                                                    }
                                                }
                                                if (dto.precedes() != null) {
                                                    for (String precedesTech : dto.precedes()) {
                                                        TechnologieNode precedesNode = technologieNodeMap.get(precedesTech);
                                                        if (precedesNode != null && !precedesNode.getParents().contains(child) && !child.getChildren().contains(precedesNode)) {
                                                            precedesNode.getParents().add(child);
                                                            child.getChildren().add(precedesNode);
                                                        }
                                                    }
                                                }
                                            }
                                            return tree;
                                        }))
                                        .toList()
                                        .toObservable()
                                )
                        )
                )
                .doOnNext(roots -> calculatePositions());
    }

    private Set<TechnologieNode> loadSet(TechnologieNode tree) {
        Set<TechnologieNode> set = new HashSet<>();
        set.add(tree);
        for (TechnologieNode child : tree.getChildren()) {
            set.addAll(loadSet(child));
        }
        return set;
    }

    public boolean areAllTechnologiesUnlocked() {
        return getUnlockedTechnologies().size() == allTechnologies.size();
    }

    private void calculatePositions() {
        int startX = 0;  // Start from the leftmost position
        positionNodes(technologieTree, startX, 0);
    }

    private int calculateSubtreeWidth(TechnologieNode node) {
        if (node.getChildren().isEmpty()) {
            return 1;
        }
        int width = 0;
        for (TechnologieNode child : node.getChildren()) {
            width += calculateSubtreeWidth(child);
        }
        return width;
    }

    private void positionNodes(List<TechnologieNode> nodes, int x, int y) {
        final int NODE_MARGIN_X = 50; // Horizontal margin between nodes
        final int NODE_MARGIN_Y = 1000; // Vertical margin between nodes
        int offsetX = x;
        for (TechnologieNode node : nodes) {
            int subtreeWidth = calculateSubtreeWidth(node) * (NODE_MARGIN_X + 350);
            int nodeX = offsetX + subtreeWidth/2 + 350;
            if (Objects.equals(node.getTechnology().id(), "ancient_district_upkeep_reduction")) {
                nodeX += 300;
            }
            node.setX(nodeX);
            node.setY(y);
            positionNodes(node.getChildren(), offsetX, y + NODE_MARGIN_Y);
            offsetX += subtreeWidth;
        }
    }
}